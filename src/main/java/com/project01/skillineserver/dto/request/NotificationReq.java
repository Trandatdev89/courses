package com.project01.skillineserver.dto.request;

import com.project01.skillineserver.enums.NotificationType;

public record NotificationReq(String title, String content, String linkAttachment, NotificationType notificationType) {
}
