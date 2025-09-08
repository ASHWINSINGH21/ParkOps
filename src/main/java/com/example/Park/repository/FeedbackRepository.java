package com.example.Park.repository;

import com.example.Park.model.Feedback1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback1,Integer> {
    List<Feedback1> findByResolvedFalse();
}
