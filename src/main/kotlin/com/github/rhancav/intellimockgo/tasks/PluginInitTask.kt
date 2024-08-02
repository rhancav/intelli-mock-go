package com.github.rhancav.intellimockgo.tasks

import com.github.rhancav.intellimockgo.cache.DirectoryAutoCompletionCache
import com.github.rhancav.intellimockgo.common.PluginConstants.Commands
import com.github.rhancav.intellimockgo.common.util.NotificationManager
import com.github.rhancav.intellimockgo.common.util.executeCommand
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.io.File

class PluginInitTask : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isInitialized) {
            checkPluginDependencies(project)
            initializeCache(project)
        }
    }


    private fun initializeCache(project: Project) {
        val file = File(project.basePath!!)
        val dirs = file.walkTopDown().filter { it.isDirectory }.map { it.path }.toMutableList()
        DirectoryAutoCompletionCache.setItems(dirs)
    }

    private fun checkPluginDependencies(project: Project) {
        val goVersionResult = Commands.GO_VERSION.executeCommand()
        if (!goVersionResult.first) {
            NotificationManager.sendNotification(
                project,
                "Golang check failed with message ${goVersionResult.second}",
                NotificationType.ERROR
            )
            return
        }
        checkMockgen(project)
    }

    private fun checkMockgen(project: Project) {
        val commandResult = Commands.GOMOCK_VERSION.executeCommand()

        if (!commandResult.first) {
            NotificationManager.sendNotification(
                project,
                "Gomock library is not installed on the system.",
                NotificationType.ERROR
            )
            return
        }
        NotificationManager.sendNotification(
            project,
            "Found Gomock library with version ${commandResult.second}",
            NotificationType.INFORMATION
        )

    }
}
