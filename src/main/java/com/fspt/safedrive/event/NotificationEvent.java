package com.fspt.safedrive.event;

/**
 * EventBut消息通知实体
 * 主要包含
 */

public class NotificationEvent {

    private String notificationType;//通知类型(用来判断是什么类型通知)
    private String message;//消息
    public String getNotificationType() {
        return notificationType;
    }
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
