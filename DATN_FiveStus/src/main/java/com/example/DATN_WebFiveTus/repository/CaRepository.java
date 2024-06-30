package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.Ca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CaRepository extends JpaRepository<Ca,Integer> {

    @Query("SELECT c FROM Ca c where c.deletedAt=true")
    List<Ca> getAllJoinFetch();

    @Modifying
    @Transactional
    @Query("update Ca c set c.deletedAt=false ")
    void  deletedAt(Integer id);
}
