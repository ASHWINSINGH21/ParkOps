package com.example.Park.Visitor;

import jakarta.persistence.*;

@Entity
@Table(name = "visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "visitor_id")
    private Integer visitor_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "user_id", nullable = false)
    private Integer userId; // This will store the ID of the user who made the entry

    @Column(name = "pass", nullable = false)
    private Boolean pass;

    public Visitor() {

    }

    public Visitor(Integer visitorId, String name, Integer age, Integer userId, Boolean pass) {
        this.visitor_id = visitorId;
        this.name = name;
        this.age = age;
        this.userId = userId;
        this.pass = pass;
    }

    public Integer getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(Integer visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }
}
