package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
//
//    @Query("Select s FROM Invoice s where s.deleted = 0")
//    List<Invoice> findAllInvoicesOnly();
//
//
//    @Transactional
//    @Query("update Invoice e set e.deleted=1 where e.id=?1")
//    @Modifying
//    public void softDelete(Integer id);
//}
    Optional<User> findByUsername(String username);
}
