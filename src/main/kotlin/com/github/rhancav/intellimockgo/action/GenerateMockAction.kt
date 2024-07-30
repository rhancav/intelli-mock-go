package com.github.rhancav.intellimockgo.action

import com.github.rhancav.intellimockgo.NotificationManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class GenerateMockAction : AnAction() {

    companion object {
        private val GO_INTERFACE_PATTERN: Pattern = Pattern.compile(
            "(?s)\\btype\\s+\\w+\\s+interface\\s*\\{.*?}"
        )
        private const val GO_EXTENSION = "go"
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        if (canProcessFile(e.project, file)) {
            try {
                val content = readFileContent(file!!)
                e.presentation.isEnabledAndVisible = containsGoInterface(content)
            } catch (ioException: IOException) {
                disableWithErrorMessage(e, "Error reading file")
            }
        } else {
            e.presentation.isEnabledAndVisible = false
        }
    }

    private fun canProcessFile(project: Project?, file:VirtualFile?) : Boolean{
        return project != null && file != null && isGoFile(file.extension)
    }

    private fun disableWithErrorMessage(e: AnActionEvent, message: String) {
        e.presentation.isEnabledAndVisible = false
        NotificationManager.sendNotification(
            e.project!!,
            message,
            NotificationType.ERROR
        )
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        println(selectedFile?.canonicalPath)
    }

    private fun isGoFile(extension: String?): Boolean {
        return extension == GO_EXTENSION
    }

    private fun readFileContent(file: VirtualFile): String {
        return String(file.contentsToByteArray(), StandardCharsets.UTF_8)
    }

    private fun containsGoInterface(content: String): Boolean {
        return GO_INTERFACE_PATTERN.matcher(content).find()
    }
}
