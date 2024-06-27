package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.entity.NuocUong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NuocUongRepository extends JpaRepository<NuocUong,Integer> {
}
