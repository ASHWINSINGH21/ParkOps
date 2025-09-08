package com.example.Park.Ride;

import jakarta.persistence.*;

@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ride_id")
    private Integer rideId;

    private String name;
    private String description;
    private String ticket_price;
    private Integer seat_limit;
    private Integer no_slots;

    public Ride() {
    }

    public Ride(Integer rideId, String name, String description, String ticket_price, Integer seat_limit, Integer no_slots) {
        this.rideId = rideId;
        this.name = name;
        this.description = description;
        this.ticket_price = ticket_price;
        this.seat_limit = seat_limit;
        this.no_slots = no_slots;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(String ticket_price) {
        this.ticket_price = ticket_price;
    }

    public Integer getSeat_limit() {
        return seat_limit;
    }

    public void setSeat_limit(Integer seat_limit) {
        this.seat_limit = seat_limit;
    }

    public Integer getNo_slots() {
        return no_slots;
    }

    public void setNo_slots(Integer no_slots) {
        this.no_slots = no_slots;
    }
}
