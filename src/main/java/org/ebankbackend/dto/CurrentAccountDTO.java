package org.ebankbackend.dto;

import lombok.Data;
import org.ebankbackend.enums.AccountStatus;

import java.util.Date;


@Data
public class CurrentAccountDTO extends BankAccountDTO{


    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private double overdraft;
    private CustomerDTO customer;
}
