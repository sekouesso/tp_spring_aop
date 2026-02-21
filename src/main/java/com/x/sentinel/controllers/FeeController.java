package com.x.sentinel.controllers;

import com.x.sentinel.dto.FeeComputationDto;
import com.x.sentinel.services.FeeComputationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {
    private final FeeComputationService service;

    @PostMapping("/compute")
    public FeeComputationDto compute(@RequestBody BigDecimal input) {
        return service.compute(input);
    }
}
