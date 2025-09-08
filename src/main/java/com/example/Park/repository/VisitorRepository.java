package com.example.Park.repository;

import com.example.Park.model.Visitor1;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor1, Integer> {
    List<Visitor1> findByUid(int uid);
}
