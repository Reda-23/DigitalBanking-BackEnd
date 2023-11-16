package org.ebankbackend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("CA")
@Data
public class CurrentAccount extends BankAccount {

    private double overdraft;
}
