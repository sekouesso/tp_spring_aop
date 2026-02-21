package com.x.sentinel.services.impl;

import com.x.sentinel.entities.Transfer;
import com.x.sentinel.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(Transfer transfer) {
        log.info(" Message envoyé {}",transfer);
    }
}