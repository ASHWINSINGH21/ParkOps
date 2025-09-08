package com.example.Park.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "visitor")
public class Visitor1 {
    @Id
    int visitor_id;

    int age;

    @Column(name = "user_id")
    int uid;

//    @Column(name = "visitor_name")
    String name;

//    boolean pass=;

    public Visitor1() {
    }

    public Visitor1(int visitor_id, int age, int uid, String name) {
        this.visitor_id = visitor_id;
        this.age = age;
        this.uid = uid;
        this.name = name;
//        this.pass = pass;
    }

    public int getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(int visitor_id) {
        this.visitor_id = visitor_id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public boolean isPass() {
//        return pass;
//    }
//
//    public void setPass(boolean pass) {
//        this.pass = pass;
//    }
}
