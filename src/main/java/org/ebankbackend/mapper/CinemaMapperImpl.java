package org.ebankbackend.mapper;

import org.ebankbackend.dto.CurrentAccountDTO;
import org.ebankbackend.dto.CustomerDTO;
import org.ebankbackend.dto.OperationDTO;
import org.ebankbackend.dto.SavingAccountDTO;
import org.ebankbackend.model.CurrentAccount;
import org.ebankbackend.model.Customer;
import org.ebankbackend.model.Operation;
import org.ebankbackend.model.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CinemaMapperImpl implements CinemaMapper {


    @Override
    public CustomerDTO fromCustomerToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }

    @Override
    public Customer fromDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    @Override
    public CurrentAccountDTO fromCurrentAccountToDTO(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentAccountDTO);
        currentAccountDTO.setCustomer(fromCustomerToDTO(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    @Override
    public CurrentAccount fromDTOToCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO,currentAccount);
        currentAccount.setCustomer(fromDTOToCustomer(currentAccountDTO.getCustomer()));
        return currentAccount;
    }

    @Override
    public SavingAccountDTO fromSavingAccountToDTO(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingAccountDTO);
        savingAccountDTO.setCustomer(fromCustomerToDTO(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    @Override
    public SavingAccount fromDTOToSavingAccount(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO,savingAccount);
        savingAccount.setCustomer(fromDTOToCustomer(savingAccountDTO.getCustomer()));
        return savingAccount;
    }

    @Override
    public OperationDTO fromOperationToDTO(Operation operation) {
        OperationDTO operationDTO = new OperationDTO();
        BeanUtils.copyProperties(operation,operationDTO);
        return operationDTO;
    }

    @Override
    public Operation fromDTOToOperation(OperationDTO operationDTO) {
        Operation operation = new Operation();
        BeanUtils.copyProperties(operationDTO,operation);
        return operation;
    }
}
