package com.example.Park.TicketBooking;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Ride_ticket_master {
    @Transient
    private List<Long> visitorIds;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticket_id;

    @Column(name = "visitor_id")
    private Integer visitorId;
    @Column(name = "ride_id")
    private Integer rideId;

    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
    private int slot;

    public Ride_ticket_master() {
    }

    public Ride_ticket_master(List<Long> visitorIds, Integer ticket_id, Integer visitorId, Integer rideId, LocalDate date, LocalTime time, int slot) {
        this.visitorIds = visitorIds;
        this.ticket_id = ticket_id;
        this.visitorId = visitorId;
        this.rideId = rideId;
        this.date = date;
        this.time = time;
        this.slot = slot;
    }

    public List<Long> getVisitorIds() {
        return visitorIds;
    }

    public void setVisitorIds(List<Long> visitorIds) {
        this.visitorIds = visitorIds;
    }

    public Integer getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(Integer ticket_id) {
        this.ticket_id = ticket_id;
    }

    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
