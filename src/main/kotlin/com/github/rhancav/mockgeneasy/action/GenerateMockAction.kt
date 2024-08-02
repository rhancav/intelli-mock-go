package com.github.rhancav.mockgeneasy.action

import com.github.rhancav.mockgeneasy.common.PluginConstants.MockGen
import com.github.rhancav.mockgeneasy.common.PluginConstants.UITexts
import com.github.rhancav.mockgeneasy.common.util.NotificationManager
import com.github.rhancav.mockgeneasy.common.util.executeCommand
import com.github.rhancav.mockgeneasy.ui.GenerateMockDialog
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class GenerateMockAction : AnAction() {

    companion object {
        private val GO_INTERFACE_PATTERN: Pattern = Pattern.compile(
            MockGen.INTERFACE_REGEX
        )
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (canProcessFile(e.project, file)) {
            try {
                val content = readFileContent(file!!)
                e.presentation.isEnabled = containsGoInterface(content)
            } catch (ioException: IOException) {
                disableWithErrorMessage(e, ioException.message!!)
            }
        } else {
            e.presentation.isEnabled = false
        }
    }

    private fun canProcessFile(project: Project?, file: VirtualFile?): Boolean {
        return project != null && file != null && isGoFile(file.extension)
    }

    private fun disableWithErrorMessage(e: AnActionEvent, message: String) {
        e.presentation.isEnabled = false
        NotificationManager.sendNotification(
            e.project!!,
            message,
            NotificationType.ERROR
        )
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.project ?: return

        val dialog = GenerateMockDialog(project, file!!)
        if (dialog.showAndGet()) {
            val selectedDirectory = dialog.getSelectedDirectory()
            val packageName = dialog.getPackageName()

            ProgressManager.getInstance()
                .run(object : Task.Backgroundable(project, UITexts.ACTION_TITLE, true) {
                    override fun run(indicator: ProgressIndicator) {
                        indicator.text = "Generating mock for ${file.name}"
                        val genCmd = buildMockgenCommand(file, selectedDirectory, packageName)
                        val (isSuccess, message) = genCmd.executeCommand(selectedDirectory)
                        indicator.text =
                            "Mock generation ${if (isSuccess) "successful" else "failed"}"
                        NotificationManager.sendNotification(
                            project,
                            if (isSuccess) "Mock generated successfully" else "Mock generation failed: $message",
                            if (isSuccess) NotificationType.INFORMATION else NotificationType.ERROR

                        )
                        if (isSuccess) {
                            val virtualDirectory = LocalFileSystem.getInstance()
                                .refreshAndFindFileByPath(selectedDirectory)
                            if (virtualDirectory != null) {
                                VfsUtil.markDirtyAndRefresh(false, true, true, virtualDirectory)
                            }
                        }
                    }
                })
        }
    }

    private fun buildMockgenCommand(
        file: VirtualFile,
        directory: String,
        packageName: String
    ): String {
        val commandTemplate = MockGen.COMMAND_TEMPLATE
        val destinationDir = File(directory)
        if (!destinationDir.exists()) {
            destinationDir.mkdirs()
        }
        val filepath = file.canonicalPath
        val destinationFile = "${file.nameWithoutExtension}${MockGen.MOCK_SUFFIX}"
        val destinationPath = directory + File.separator + destinationFile
        return String.format(commandTemplate, filepath, destinationPath, packageName)
    }

    private fun isGoFile(extension: String?): Boolean {
        return extension == MockGen.GO_EXTENSION
    }

    private fun readFileContent(file: VirtualFile): String {
        return String(file.contentsToByteArray(), StandardCharsets.UTF_8)
    }

    private fun containsGoInterface(content: String): Boolean {
        return GO_INTERFACE_PATTERN.matcher(content).find()
    }
}
