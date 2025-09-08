package com.example.Park.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface user_master_repo extends JpaRepository<User_master, Integer> {
    User_master findByEmail(String email);

    User_master findByUserName(String userName);
}
