package com.x.sentinel.services;

import com.x.sentinel.dto.TransferCreateDto;
import com.x.sentinel.dto.TransferResponseDto;

public interface TransferService {
    // private final NotificationService notificationService; // stub
    TransferResponseDto createTransfer(TransferCreateDto dto);
}
