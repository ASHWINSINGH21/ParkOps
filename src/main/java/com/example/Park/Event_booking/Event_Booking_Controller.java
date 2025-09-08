package com.example.Park.Event_booking;

import com.example.Park.Event.Event_repo;
import com.example.Park.User.User_master;
import com.example.Park.User.user_master_repo;
import com.example.Park.Visitor.Visitor_repo;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class Event_Booking_Controller {

    @Autowired
    private Event_repo eventRepo;

    @Autowired
    private Visitor_repo visitorRepo;

    @Autowired
    private Event_booking_repo eventBookingRepo;

    @Autowired
    private user_master_repo userRepo;

    @GetMapping("/event_book")
    public String showEventBookingForm(Model model, Principal principal, HttpSession session) {
        User_master user = userRepo.findByEmail(principal.getName());

        model.addAttribute("booking", new Event_Booking());
        model.addAttribute("events", eventRepo.findAll());
        model.addAttribute("visitors", visitorRepo.findByUserId(user.getUser_id()));

        Boolean firstTimeBooking = (Boolean) session.getAttribute("firstTimeBooking");
        if (firstTimeBooking == null) firstTimeBooking = true;
        session.setAttribute("firstTimeBooking", firstTimeBooking);
        model.addAttribute("firstTimeBooking", firstTimeBooking);

        Boolean hasBooked = (Boolean) session.getAttribute("hasBookedInSession");
        if (hasBooked == null) hasBooked = false;
        session.setAttribute("hasBookedInSession", hasBooked);
        model.addAttribute("hasBooked", hasBooked);

        return "Book_event";
    }

    @PostMapping("/event_booked")
    public String bookEvent(@ModelAttribute("booking") Event_Booking booking,
                            Principal principal,
                            HttpSession session,
                            Model model) {

        User_master user = userRepo.findByEmail(principal.getName());
        if (user == null) {
            model.addAttribute("errorMessage", "Please login to book events.");
            return showEventBookingForm(model, principal, session);
        }

        List<String> alreadyBooked = new ArrayList<>();
        List<String> newlyBooked = new ArrayList<>();
        List<String> duplicateSelected = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        if (booking.getVisitorIds() == null || booking.getVisitorIds().isEmpty()) {
            model.addAttribute("errorMessage", "Please select at least one visitor.");
            return showEventBookingForm(model, principal, session);
        }

        LocalDate date = booking.getDate();
        LocalTime time = booking.getTime();
        if (time != null) time = time.withSecond(0).withNano(0);

        for (Integer visitorId : booking.getVisitorIds()) {
            if (visitorId == null) continue;

            if (!seen.add(visitorId)) {
                visitorRepo.findById(visitorId).ifPresent(v -> duplicateSelected.add(v.getName()));
                continue;
            }

            boolean exists = eventBookingRepo.existsByVisitorIdAndEventIdAndDateAndTime(
                    visitorId, booking.getEventId(), date, time
            );

            if (exists) {
                visitorRepo.findById(visitorId).ifPresent(v -> alreadyBooked.add(v.getName()));
                continue;
            }

            Event_Booking newBooking = new Event_Booking();
            newBooking.setVisitorId(visitorId);
            newBooking.setEventId(booking.getEventId());
            newBooking.setDate(date);
            newBooking.setTime(time);

            eventRepo.findById(booking.getEventId())
                    .ifPresent(event -> newBooking.setAmount(event.getAmount()));

            try {
                eventBookingRepo.saveAndFlush(newBooking);
                visitorRepo.findById(visitorId).ifPresent(v -> newlyBooked.add(v.getName()));
            } catch (DataIntegrityViolationException e) {
                visitorRepo.findById(visitorId).ifPresent(v -> alreadyBooked.add(v.getName()));
            }
        }

        if (!newlyBooked.isEmpty()) {
            session.setAttribute("firstTimeBooking", false);
            session.setAttribute("hasBookedInSession", true);
        }

        if (!newlyBooked.isEmpty())
            model.addAttribute("successMessage", "Tickets booked for: " + String.join(", ", newlyBooked));
        if (!alreadyBooked.isEmpty())
            model.addAttribute("errorMessage", "Already booked visitors: " + String.join(", ", alreadyBooked));
        if (!duplicateSelected.isEmpty())
            model.addAttribute("warningMessage", "Duplicate selection: " + String.join(", ", duplicateSelected));

        return showEventBookingForm(model, principal, session);
    }
}

