package com.example.Park.Event_booking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;

public interface Event_booking_repo extends JpaRepository<Event_Booking, Integer> {
    boolean existsByVisitorIdAndEventIdAndDateAndTime(Integer visitorId, Integer eventId, LocalDate date, LocalTime time);
}
