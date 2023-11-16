package org.ebankbackend.dto;

import lombok.Data;

@Data
public class TransferDTO {

    private String idSource;
    private String idDestination;
    private double amount;
    private String description;
}
