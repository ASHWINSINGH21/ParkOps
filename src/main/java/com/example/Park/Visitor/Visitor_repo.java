package com.example.Park.Visitor;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface Visitor_repo extends JpaRepository<Visitor, Integer> {
    List<Visitor> findByUserId(Integer userId);
}
