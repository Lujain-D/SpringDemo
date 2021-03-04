package com.example.demo.repositories;

import com.example.demo.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ItemRepository extends JpaRepository<Item, Integer> {


    @Transactional
    @Query("update Item e set e.deleted=1 where e.id=?1")
    @Modifying
    public void softDelete(Integer id);



}
