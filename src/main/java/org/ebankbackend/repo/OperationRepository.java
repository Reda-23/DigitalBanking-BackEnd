package org.ebankbackend.repo;

import org.ebankbackend.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation,Long> {

    List<Operation> findOperationByBankAccountId(String accountId);
    Page<Operation> findOperationByBankAccountIdOrderByDateDesc(String accountId, Pageable pageable);
}
