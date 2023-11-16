package org.ebankbackend.service;

import org.ebankbackend.dto.*;
import org.ebankbackend.exceptions.BankAccountIsNotFoundException;
import org.ebankbackend.exceptions.CustomerNotFoundException;
import org.ebankbackend.exceptions.SoldeNotSufficientException;
import org.ebankbackend.model.BankAccount;
import org.ebankbackend.model.CurrentAccount;
import org.ebankbackend.model.Customer;
import org.ebankbackend.model.SavingAccount;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {


    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CurrentAccountDTO addCurrentAccount(double balance, double overdraft, Long customerId) throws CustomerNotFoundException;

    SavingAccountDTO addSavingAccount(double balance, double interestRate, Long customerId) throws CustomerNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountIsNotFoundException, SoldeNotSufficientException;
    void credit(String accountId,double amount,String description) throws BankAccountIsNotFoundException;

    void virement(String idSource,String idDestination, double amount) throws SoldeNotSufficientException, BankAccountIsNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountIsNotFoundException;

    List<BankAccountDTO> bankAccounts();

    List<CustomerDTO> customers();

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException;

    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    List<OperationDTO> operationHistory(String accountId);

    HistoryDTO operationPaging(String accountId, int page, int size) throws BankAccountIsNotFoundException;

    List<CustomerDTO> searchCustomer(String keyword);
}
