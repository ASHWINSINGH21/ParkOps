package com.example.Park.TicketBooking;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface Ride_Ticket_Master_repo extends JpaRepository<Ride_ticket_master, Integer> {
    boolean existsByVisitorIdAndRideIdAndDateAndSlot(Integer visitorId, Integer rideId, LocalDate date, int slot);

    long countByRideIdAndDateAndSlot(Integer rideId, LocalDate date, int slot);
}