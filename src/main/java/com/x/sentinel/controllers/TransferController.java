package com.x.sentinel.controllers;

import com.x.sentinel.dto.TransferCreateDto;
import com.x.sentinel.dto.TransferResponseDto;
import com.x.sentinel.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDto create(@RequestBody TransferCreateDto dto) {
        return transferService.createTransfer(dto);
    }
}
