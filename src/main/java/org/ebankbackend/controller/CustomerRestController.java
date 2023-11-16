package org.ebankbackend.controller;


import lombok.RequiredArgsConstructor;
import org.ebankbackend.dto.CustomerDTO;
import org.ebankbackend.exceptions.CustomerNotFoundException;
import org.ebankbackend.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerRestController {

    private final BankAccountService bankAccountService;


    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.customers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam String keyword){

        return bankAccountService.searchCustomer("%"+keyword+"%");
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable("id") Long customerId,@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
            return bankAccountService.updateCustomer(customerId,customerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(customerId);
    }

}
