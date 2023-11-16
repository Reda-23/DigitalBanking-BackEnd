package org.ebankbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class HistoryDTO {

    private String accountId ;
    private double balance ;
    private int page ;
    private int size ;
    private int totalPages;
    private List<OperationDTO> operationDTOS;
}
