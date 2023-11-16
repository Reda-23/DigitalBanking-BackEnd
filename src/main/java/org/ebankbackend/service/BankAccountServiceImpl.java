package org.ebankbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebankbackend.dto.*;
import org.ebankbackend.enums.AccountStatus;
import org.ebankbackend.enums.OperationType;
import org.ebankbackend.exceptions.BankAccountIsNotFoundException;
import org.ebankbackend.exceptions.CustomerNotFoundException;
import org.ebankbackend.exceptions.SoldeNotSufficientException;
import org.ebankbackend.mapper.CinemaMapper;
import org.ebankbackend.model.*;
import org.ebankbackend.repo.BankAccountRepository;
import org.ebankbackend.repo.CustomerRepository;
import org.ebankbackend.repo.OperationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;
    private final CinemaMapper mapper;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.fromDTOToCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("customer added successfully");
        return mapper.fromCustomerToDTO(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()->
                new CustomerNotFoundException("customer not found"));
        return mapper.fromCustomerToDTO(customer);
    }


    @Override
    public CurrentAccountDTO addCurrentAccount(double balance, double overdraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setBalance(balance);
        currentAccount.setOverdraft(overdraft);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(customer);
        CurrentAccount addCurrentAccount = bankAccountRepository.save(currentAccount);
        return mapper.fromCurrentAccountToDTO(addCurrentAccount);
    }


    @Override
    public SavingAccountDTO addSavingAccount(double balance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setBalance(balance);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        SavingAccount addSavingAccount = bankAccountRepository.save(savingAccount);
        return mapper.fromSavingAccountToDTO(addSavingAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountIsNotFoundException, SoldeNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountIsNotFoundException("bank account is not found"));
        if (bankAccount.getBalance() < amount){
            throw new SoldeNotSufficientException("solde insufficient to for a debit");
        }
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setType(OperationType.DEBIT);
        operation.setDescription(description);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()-amount );
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountIsNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountIsNotFoundException("bank account is not found"));
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setType(OperationType.CREDIT);
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operation.setDate(new Date());
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()+amount );
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void virement(String idSource, String idDestination, double amount) throws SoldeNotSufficientException, BankAccountIsNotFoundException {
        debit(idSource,amount,"Transfer To "  + idDestination);
        credit(idDestination,amount,"Transfer From " + idSource);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountIsNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountIsNotFoundException("bank account is not found"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount  = (SavingAccount) bankAccount;
            return mapper.fromSavingAccountToDTO(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return mapper.fromCurrentAccountToDTO(currentAccount);
        }
    }

    @Override
    public List<BankAccountDTO> bankAccounts(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
       List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
           if (bankAccount instanceof SavingAccount){
               SavingAccount savingAccount = (SavingAccount) bankAccount;
               return mapper.fromSavingAccountToDTO(savingAccount);
           }else {
               CurrentAccount currentAccount = (CurrentAccount) bankAccount;
               return mapper.fromCurrentAccountToDTO(currentAccount);
           }
       }).collect(Collectors.toList());
       return bankAccountDTOS;
    }

    @Override
    public List<CustomerDTO> customers(){
        List<Customer> customer = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customer.stream()
                .map(cust -> mapper.fromCustomerToDTO(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        CustomerDTO updCust = mapper.fromCustomerToDTO(customer);
        updCust.setName(customerDTO.getName());
        updCust.setEmail(customerDTO.getEmail());
        Customer custom = customerRepository.save(mapper.fromDTOToCustomer(updCust));
        return mapper.fromCustomerToDTO(custom);

    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public List<OperationDTO> operationHistory(String accountId){
        List<Operation> operations = operationRepository.findOperationByBankAccountId(accountId);
        List<OperationDTO> operationDTOS = operations.stream().map(op-> mapper.fromOperationToDTO(op)).toList();
        return operationDTOS;
    }


    @Override
    public HistoryDTO operationPaging(String accountId, int page, int size) throws BankAccountIsNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountIsNotFoundException("bank account not found"));
        Page<Operation> operations = operationRepository.findOperationByBankAccountIdOrderByDateDesc(accountId, PageRequest.of(page,size));
        List<OperationDTO> operationDTOS = operations.getContent().stream().map(op -> mapper.fromOperationToDTO(op)).collect(Collectors.toList());
       HistoryDTO historyDTO = new HistoryDTO();
       historyDTO.setAccountId(bankAccount.getId());
       historyDTO.setPage(page);
       historyDTO.setBalance(bankAccount.getBalance());
       historyDTO.setOperationDTOS(operationDTOS);
       historyDTO.setSize(size);
       historyDTO.setTotalPages(operations.getTotalPages());
       return historyDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String keyword){
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(c -> mapper.fromCustomerToDTO(c)).collect(Collectors.toList());
        return customerDTOS;
    }
}
