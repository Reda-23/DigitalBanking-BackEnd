package org.ebankbackend;

import org.ebankbackend.dto.CurrentAccountDTO;
import org.ebankbackend.dto.CustomerDTO;
import org.ebankbackend.dto.SavingAccountDTO;
import org.ebankbackend.exceptions.BankAccountIsNotFoundException;
import org.ebankbackend.exceptions.CustomerNotFoundException;
import org.ebankbackend.exceptions.SoldeNotSufficientException;
import org.ebankbackend.security.model.AppRole;
import org.ebankbackend.security.model.AppUser;
import org.ebankbackend.service.BankAccountService;
import org.ebankbackend.service.UserSecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class EBankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankBackendApplication.class, args);
    }



    @Bean
    CommandLineRunner run(BankAccountService accountService , UserSecurityService userSecurityService){
        return args -> {
            Stream.of("Reda","Imane","Simo").forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@yahoo.com");
                accountService.saveCustomer(customer);
            });

            accountService.customers().forEach(customer -> {
                try {
                    accountService.addCurrentAccount(Math.random()*13000,5400,customer.getId());
                    accountService.addSavingAccount(Math.random()*14000,4.5, customer.getId());
                    accountService.bankAccounts().forEach(bankAccount -> {
                        try {
                            String accountId;
                            if (bankAccount instanceof SavingAccountDTO){
                               accountId = ((SavingAccountDTO) bankAccount).getId();
                            }else {
                                accountId = ((CurrentAccountDTO) bankAccount).getId();
                            }
                            for (int i = 0; i <= 10 ; i ++){
                            accountService.credit(accountId,1000+Math.random()*120000,"Crediter d'un compte");
                            accountService.debit(accountId,1000+Math.random()*9000,"Debiter d'un compte ");}
                        } catch (BankAccountIsNotFoundException | SoldeNotSufficientException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }

            });


            userSecurityService.addUser(new AppUser("Reda","Hajjaj","redahajjaj@gmail.com","1234"));
            userSecurityService.addUser(new AppUser("Simo","Elharrad","simoelharrad@gmail.com","1234"));

            userSecurityService.addRole(new AppRole(null,"USER"));
            userSecurityService.addRole(new AppRole(null,"ADMIN"));
            userSecurityService.addRole(new AppRole(null,"MANAGER"));

            userSecurityService.setRoleToUser("redahajjaj@gmail.com","USER");
            userSecurityService.setRoleToUser("redahajjaj@gmail.com","ADMIN");
            userSecurityService.setRoleToUser("simoelharrad@gmail.com","USER");





        };
    }

}
