package com.example.Park.Event;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eveseq")
    @SequenceGenerator(name = "eveseq", sequenceName = "eveseq", allocationSize = 1)
    @Column(name = "event_id")
    private int eventId;

    private String name;
    private LocalDate start_date;
    private LocalDate end_date;
    private String description;
    private int amount;

    public Event(int eventId, String name, LocalDate start_date, LocalDate end_date, String description, int amount) {
        this.eventId = eventId;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.amount = amount;
    }

    public Event() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

