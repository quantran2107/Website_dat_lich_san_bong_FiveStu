package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DoThue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoThueRepository extends JpaRepository<DoThue,Integer> {
}
