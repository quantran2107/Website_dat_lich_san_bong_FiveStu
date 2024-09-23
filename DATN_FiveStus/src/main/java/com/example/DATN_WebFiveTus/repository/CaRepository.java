package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.Ca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CaRepository extends JpaRepository<Ca,Integer> {

    @Query("SELECT c FROM Ca c where c.deletedAt=false")
    List<Ca> getAllJoinFetch();

    @Modifying
    @Transactional
    @Query("update Ca c set c.deletedAt=true ")
    void  deletedAt(Integer id);


    @Query("SELECT c FROM Ca c")
    Page<Ca> findByCaPage(Pageable pageable);

    @Query("SELECT c FROM Ca c WHERE c.id = :id OR c.tenCa=:keyWords OR c.trangThai=:keyWords")
    Page<Ca> search(@Param("id") Integer id, @Param("keyWords") String keyWords, Pageable pageable);

}
