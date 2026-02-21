package com.x.sentinel.services;

import com.x.sentinel.entities.Transfer;

public interface NotificationService {
    void sendNotification(Transfer transfer);
}
