package org.ebankbackend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebankbackend.dto.*;
import org.ebankbackend.exceptions.BankAccountIsNotFoundException;
import org.ebankbackend.exceptions.SoldeNotSufficientException;
import org.ebankbackend.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/bank")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping("/accounts")
    public List<BankAccountDTO> bankAccountDTOS(){
        return bankAccountService.bankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccountDTO findBankAccount(@PathVariable(name = "id") String accountId) throws BankAccountIsNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/history/{id}")
    public List<OperationDTO> operationHistory(@PathVariable(name = "id") String accountId){
        return bankAccountService.operationHistory(accountId);
    }

    @GetMapping("/operations/{id}")
    public HistoryDTO operationListPaging(@PathVariable(name = "id") String accountId,
                                                 @RequestParam(name = "page" , defaultValue = "0") int page ,
                                                  @RequestParam(name = "size" , defaultValue = "5") int size) throws BankAccountIsNotFoundException {

        HistoryDTO historyDTO = bankAccountService.operationPaging(accountId, page, size);
        return historyDTO;
    }

    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws SoldeNotSufficientException, BankAccountIsNotFoundException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountIsNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/transfer")
    public TransferDTO transfer(@RequestBody TransferDTO transferDTO) throws SoldeNotSufficientException, BankAccountIsNotFoundException {
        this.bankAccountService.virement(transferDTO.getIdSource(), transferDTO.getIdDestination(), transferDTO.getAmount());
        return transferDTO;
    }
}
