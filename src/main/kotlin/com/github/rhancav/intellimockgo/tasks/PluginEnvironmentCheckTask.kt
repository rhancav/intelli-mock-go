package com.github.rhancav.intellimockgo.tasks

import com.github.rhancav.intellimockgo.NotificationManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.slf4j.LoggerFactory
import java.io.BufferedReader

class PluginEnvironmentCheckTask : ProjectActivity {

    companion object {
        private val logger = LoggerFactory.getLogger(PluginEnvironmentCheckTask::class.java)
    }

    override suspend fun execute(project: Project) {
        if (project.isInitialized) {
            checkCommandAndNotifyUser("go version", "Go", project)
            checkCommandAndNotifyUser("mockgen --version", "mockgen", project)
        }
    }

    private fun checkCommandAndNotifyUser(command: String, toolName: String, project: Project) {
        val commandResult = getCommandOutput(command)

        logger.info("$toolName version check output: ${commandResult.second}")

        if (!commandResult.first) {
            NotificationManager.sendNotification(
                project,
                "$toolName is not installed on the system.",
                NotificationType.ERROR
            )
        } else {
            NotificationManager.sendNotification(
                project,
                "Found $toolName with version ${commandResult.second}",
                NotificationType.INFORMATION
            )
        }
    }

    private fun getCommandOutput(command: String): Pair<Boolean, String> {
        val shellCommand = getShellCommand(command)
        return try {
            val processBuilder = ProcessBuilder(*shellCommand)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val output = process.inputStream.bufferedReader().use(BufferedReader::readText).trim()
            val isSuccess = process.waitFor() == 0
            Pair(isSuccess, output)
        } catch (e: Exception) {
            logger.error("Unexpected error: $e")
            Pair(false, e.toString())
        }
    }

    private fun getShellCommand(command: String): Array<String> {
        return if (isWindows()) {
            arrayOf("cmd.exe", "/c", command)
        } else {
            arrayOf("/bin/sh", "-c", command)
        }
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name").lowercase().contains("win")
    }
}