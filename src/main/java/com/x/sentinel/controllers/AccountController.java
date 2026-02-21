package com.x.sentinel.controllers;

import com.x.sentinel.dto.AccountDto;
import com.x.sentinel.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @PostMapping
    public AccountDto create(@RequestBody AccountDto dto) {
        return service.createAccount(dto);
    }

    @GetMapping("/balance")
    public Object getBalance(@RequestParam UUID id){
        return service.getBalance(id);
    }

    @GetMapping()
    public Object getDonnees(){
        return service.getDonnees();
    }

}
