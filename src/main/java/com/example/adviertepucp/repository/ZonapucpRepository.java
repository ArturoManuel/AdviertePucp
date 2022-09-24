package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Zonapucp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonapucpRepository extends JpaRepository<Zonapucp,Integer>{
}
