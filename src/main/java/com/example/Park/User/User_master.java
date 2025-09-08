package com.example.Park.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public class User_master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    @Column(name = "user_name")
    private String userName;
    @Email()
    private String email;
    private String password;
    private String contact;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return userName;
    }

    public void setUser_name(String user_name) {
        this.userName = user_name;
    }

    public @Email() String getEmail() {
        return email;
    }

    public void setEmail(@Email() String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User_master(int user_id, String userName, String email, String password, String contact) {
        this.user_id = user_id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.contact = contact;
    }

    public User_master() {
    }
}
