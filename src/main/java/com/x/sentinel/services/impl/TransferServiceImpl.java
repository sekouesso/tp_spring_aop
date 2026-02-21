package com.x.sentinel.services.impl;

import com.x.sentinel.annotation.Audited;
import com.x.sentinel.annotation.SecureAudit;
import com.x.sentinel.dto.TransferCreateDto;
import com.x.sentinel.dto.TransferResponseDto;
import com.x.sentinel.entities.Account;
import com.x.sentinel.entities.Transfer;
import com.x.sentinel.enums.TransferStatus;
import com.x.sentinel.exception.InsufficientBalanceException;
import com.x.sentinel.exception.SameAccountTransferException;
import com.x.sentinel.repositories.AccountRepository;
import com.x.sentinel.repositories.TransferRepository;
import com.x.sentinel.services.NotificationService;
import com.x.sentinel.services.TransferService;
import com.x.sentinel.utils.SimulatedUserContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final NotificationService notificationService;
    @Override
    @SecureAudit(action = "TRANSFER_CREATE", sensitive = true)
    public TransferResponseDto createTransfer(TransferCreateDto dto) {

        if (dto.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant ne doit pas être négatif");
        }

        Account from = accountRepository.findById(dto.fromAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Compte débiteur introuvable"));

        Account to = accountRepository.findById(dto.toAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Compte créditeur introuvable"));

        if (from.getId().equals(to.getId())) {
            throw new SameAccountTransferException("Le virement intra-compte n'est pas autorisé");
        }

        if (!from.getCurrency().equals(dto.currency()) || !to.getCurrency().equals(dto.currency())) {
            throw new IllegalArgumentException("La devise doit être identique sur les deux comptes");
        }

        if (from.getBalance().compareTo(dto.amount()) < 0) {
            throw new InsufficientBalanceException("Solde insuffisant");
        }

        Transfer transfer = Transfer.builder()
                .reference("TRF-" + UUID.randomUUID().toString().substring(0,8).toUpperCase())
                .fromAccountId(from.getId())
                .toAccountId(to.getId())
                .amount(dto.amount())
                .currency(dto.currency())
                .label(dto.label())
                .createdBy(SimulatedUserContext.getCurrentUser().username())
                .status(TransferStatus.PENDING)
                .build();

        transfer = transferRepository.save(transfer);

        try {
            from.setBalance(from.getBalance().subtract(dto.amount()));
            to.setBalance(to.getBalance().add(dto.amount()));

            accountRepository.save(from);
            accountRepository.save(to);

            transfer.setStatus(TransferStatus.SUCCESS);
            transfer.setExecutedAt(LocalDateTime.now());
        } catch (Exception e) {
            transfer.setStatus(TransferStatus.FAILED);
            transfer.setFailureCode("ERR_EXEC");
            transfer.setFailureMessage(e.getMessage());
        }

        transfer = transferRepository.save(transfer);

         notificationService.sendNotification(transfer); // async ou fire-and-forget

        return mapToResponseDto(transfer);
    }


    private TransferResponseDto mapToResponseDto(Transfer t) {
        return new TransferResponseDto(
                t.getId(), t.getReference(), t.getFromAccountId(), t.getToAccountId(),
                t.getAmount(), t.getCurrency(), t.getLabel(), t.getStatus(),
                t.getRequestedAt(), t.getExecutedAt(), t.getFailureCode(), t.getFailureMessage()
        );
    }
}
