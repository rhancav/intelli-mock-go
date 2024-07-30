package com.github.rhancav.intellimockgo

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class NotificationManager {
    companion object {
        private const val NOTIFICATION_GROUP_NAME = "intelli-mock-go-notifications"
        private val notificationGroup =
            NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP_NAME)

        fun sendNotification(p: Project, message: String, type: NotificationType) {
            val notification = notificationGroup.createNotification(message, type)
            notification.notify(p)
        }
    }
}