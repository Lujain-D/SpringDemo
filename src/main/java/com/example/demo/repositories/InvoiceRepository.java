package com.example.demo.repositories;

import com.example.demo.entities.Invoice;
import com.example.demo.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, PagingAndSortingRepository<Invoice,Integer> {
    @Query("Select s FROM Invoice s where s.deleted = 0")
    List<Invoice> findAllInvoicesOnly();


    @Transactional
    @Query("update Invoice e set e.deleted=1 where e.id=?1")
    @Modifying
    public void softDelete(Integer id);

    List<Invoice> findAllByUser(User user, Pageable pageable);

    @Query("SELECT i FROM Invoice i where deleted=0")
    List<Invoice> findAllPaginated(Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE CONCAT(i.name, i.number, i.total, i.issued) LIKE %?1% and user_id = ?2 and deleted=0")
    public List<Invoice> search(String keyword, Integer user_id, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE CONCAT(i.name, i.number, i.total, i.issued, i.fileLocation, i.fileLastCreated, i.fileLastUpdated, i.fileSize) LIKE %?1% and deleted=0")
    public List<Invoice> search(String keyword, Pageable pageable);

}
