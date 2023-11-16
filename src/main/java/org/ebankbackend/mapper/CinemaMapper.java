package org.ebankbackend.mapper;

import org.ebankbackend.dto.CurrentAccountDTO;
import org.ebankbackend.dto.CustomerDTO;
import org.ebankbackend.dto.OperationDTO;
import org.ebankbackend.dto.SavingAccountDTO;
import org.ebankbackend.model.CurrentAccount;
import org.ebankbackend.model.Customer;
import org.ebankbackend.model.Operation;
import org.ebankbackend.model.SavingAccount;

public interface CinemaMapper {


    CustomerDTO fromCustomerToDTO(Customer customer);
    Customer fromDTOToCustomer(CustomerDTO customerDTO);

    CurrentAccountDTO fromCurrentAccountToDTO(CurrentAccount currentAccount);
    CurrentAccount fromDTOToCurrentAccount(CurrentAccountDTO currentAccountDTO);

    SavingAccountDTO fromSavingAccountToDTO(SavingAccount savingAccount);
    SavingAccount fromDTOToSavingAccount(SavingAccountDTO savingAccountDTO);

    OperationDTO fromOperationToDTO(Operation operation);
    Operation fromDTOToOperation(OperationDTO operationDTO);
}
