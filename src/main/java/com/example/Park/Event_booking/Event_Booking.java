package com.example.Park.Event_booking;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Event_Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_id;

    @Transient
    private List<Integer> visitorIds; // for form binding, not persisted

    @Column(name = "visitor_id")
    private Integer visitorId;

    @Column(name = "event_id")
    private Integer eventId;
    private Integer amount;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    private LocalDate date;

    public Integer getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Integer booking_id) {
        this.booking_id = booking_id;
    }

    public List<Integer> getVisitorIds() {
        return visitorIds;
    }

    public void setVisitorIds(List<Integer> visitorIds) {
        this.visitorIds = visitorIds;
    }

    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Event_Booking() {
    }

    public Event_Booking(Integer booking_id, List<Integer> visitorIds, Integer visitorId, Integer eventId, Integer amount, LocalTime time, LocalDate date) {
        this.booking_id = booking_id;
        this.visitorIds = visitorIds;
        this.visitorId = visitorId;
        this.eventId = eventId;
        this.amount = amount;
        this.time = time;
        this.date = date;
    }
}
