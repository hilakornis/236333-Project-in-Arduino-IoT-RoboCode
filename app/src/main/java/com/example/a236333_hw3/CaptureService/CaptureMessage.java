package com.example.a236333_hw3.CaptureService;

import java.util.Date;

public class CaptureMessage {
    public String senderId;
    public String senderName;
    public String recipientId;
    public String taskId;

    public CaptureMessage(String _senderId,
            String _senderName,
            String _recipientId,
            String _taskId) {
        senderId = _senderId;
        senderName = _senderName;
        recipientId = _recipientId;
        taskId = _taskId;
    }

    public CaptureMessage() {
        this("","","", "");
    }
}
