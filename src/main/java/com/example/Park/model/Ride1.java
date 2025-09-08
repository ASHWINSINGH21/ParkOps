package com.example.Park.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ride")
public class Ride1 {
    @Id
    int ride_id;

    @Column(name="name")
    String ride_name ;
    String description;

    public Ride1() {
    }

    public Ride1(int ride_id, String ride_name, String description, float ticket_price) {
        this.ride_id = ride_id;
        this.ride_name = ride_name;
        this.description = description;
//        this.image = image;
//        this.ticket_price = ticket_price;
    }

    public int getRide_id() {
        return ride_id;
    }

    public void setRide_id(int ride_id) {
        this.ride_id = ride_id;
    }

    public String getRide_name() {
        return ride_name;
    }

    public void setRide_name(String ride_name) {
        this.ride_name = ride_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

//    public float getTicket_price() {
//        return ticket_price;
//    }
//
//    public void setTicket_price(float ticket_price) {
//        this.ticket_price = ticket_price;
//    }
}
