package com.example.demo.repositories;

import com.example.demo.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query("Select s FROM Invoice s where s.deleted = 0")
    List<Invoice> findAllInvoicesOnly();


    @Transactional
    @Query("update Invoice e set e.deleted=1 where e.id=?1")
    @Modifying
    public void softDelete(Integer id);
}
