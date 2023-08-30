package com.yth.ai.codereview;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import javax.annotation.Nullable;

public class PluginNotifier {

    private static final NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup("Custom Notification Group", NotificationDisplayType.BALLOON, true);

    public static void notifyError(@Nullable Project project, String content) {
        NOTIFICATION_GROUP.createNotification(content, NotificationType.WARNING)
                .notify(project);
    }

    public static void notifySuccess(@Nullable Project project, String content) {
        NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }

}