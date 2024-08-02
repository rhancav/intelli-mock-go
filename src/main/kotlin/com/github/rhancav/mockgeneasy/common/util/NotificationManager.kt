package com.github.rhancav.mockgeneasy.common.util


import com.github.rhancav.mockgeneasy.common.PluginConstants.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class NotificationManager {
    companion object {
        private val notificationGroup =
            NotificationGroupManager.getInstance()
                .getNotificationGroup(Notification.NOTIFICATION_GROUP)

        fun sendNotification(p: Project, message: String, type: NotificationType) {
            val notification =
                notificationGroup.createNotification(Notification.NOTIFICATION_TITLE, message, type)
            notification.notify(p)
        }
    }
}