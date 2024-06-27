package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ThamSo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThamSoRepository extends JpaRepository<ThamSo,Integer> {
}
