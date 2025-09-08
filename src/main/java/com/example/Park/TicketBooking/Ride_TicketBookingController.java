package com.example.Park.TicketBooking;

import com.example.Park.Ride.Ride;
import com.example.Park.Ride.Ride_repo;
import com.example.Park.User.User_master;
import com.example.Park.User.user_master_repo;
import com.example.Park.Visitor.Visitor_repo;
import com.example.Park.Mail.MailService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class Ride_TicketBookingController {

    @Autowired
    private MailService mailService;

    @Autowired
    private Ride_Ticket_Master_repo bookingRepo;

    @Autowired
    private user_master_repo userRepo;

    @Autowired
    private Visitor_repo visitorRepo;

    @Autowired
    private Ride_repo rideRepo;

    @GetMapping("/book")
    public String showBookingForm(Model model, Principal principal, HttpSession session,
                                  @RequestParam(name="rideId", required=false) Integer rideId,
                                  @RequestParam(name="date", required=false) String date,
                                  @RequestParam(name="slot", required=false) Integer slot) {

        User_master user = userRepo.findByEmail(principal.getName());
        model.addAttribute("ticket", new Ride_ticket_master());
        model.addAttribute("rides", rideRepo.findAll());
        model.addAttribute("visitors", visitorRepo.findByUserId(user.getUser_id()));

        Boolean firstTimeBooking = (Boolean) session.getAttribute("firstTimeBooking");
        if (firstTimeBooking == null) firstTimeBooking = true;
        session.setAttribute("firstTimeBooking", firstTimeBooking);
        model.addAttribute("firstTimeBooking", firstTimeBooking);

        Boolean hasBooked = (Boolean) session.getAttribute("hasBookedInSession");
        if (hasBooked == null) hasBooked = false;
        session.setAttribute("hasBookedInSession", hasBooked);
        model.addAttribute("hasBooked", hasBooked);

        if (rideId != null && date != null && slot != null) {
            Optional<Ride> rOpt = rideRepo.findById(rideId);
            if (rOpt.isPresent()) {
                int seatLimit = rOpt.get().getSeat_limit() != null ? rOpt.get().getSeat_limit() : 0;
                long booked = bookingRepo.countByRideIdAndDateAndSlot(rideId, LocalDate.parse(date), slot);
                int remaining = Math.max(0, seatLimit - (int) booked);
                model.addAttribute("remainingSeats", remaining);
                model.addAttribute("availabilityChecked", true);
                model.addAttribute("selectedRideId", rideId);
                model.addAttribute("selectedDate", date);
                model.addAttribute("selectedSlot", slot);
            }
        }

        return "Ticket_Booking_Form";
    }

    @GetMapping("/payment")
    public String paymentPage() {
        return "payment";
    }

    @GetMapping("/api/availability")
    @ResponseBody
    public Map<String, Object> availability(@RequestParam Integer rideId,
                                            @RequestParam String date,
                                            @RequestParam Integer slot) {
        Map<String, Object> out = new HashMap<>();
        Optional<Ride> rOpt = rideRepo.findById(rideId);
        if (rOpt.isEmpty()) {
            out.put("error", "Ride not found");
            return out;
        }
        Ride r = rOpt.get();
        int seatLimit = r.getSeat_limit() != null ? r.getSeat_limit() : 0;

        LocalDate ld;
        try {
            ld = LocalDate.parse(date);
        } catch (Exception ex) {
            out.put("error", "Invalid date");
            return out;
        }

        long booked = bookingRepo.countByRideIdAndDateAndSlot(rideId, ld, slot);
        int remaining = Math.max(0, seatLimit - (int) booked);

        out.put("seatLimit", seatLimit);
        out.put("booked", booked);
        out.put("remaining", remaining);
        return out;
    }

    @PostMapping("/booked")
    @Transactional
    public String bookTicket(@ModelAttribute("ticket") Ride_ticket_master ticket,
                             Principal principal,
                             HttpSession session,
                             Model model,
                             @RequestParam(name = "partialConfirm", required = false) Boolean partialConfirm) {

        List<String> alreadyBooked = new ArrayList<>();
        List<String> newlyBookedNames = new ArrayList<>();
        List<Map<String, Object>> newlyBookedForEmail = new ArrayList<>();
        List<String> duplicateSelected = new ArrayList<>();
        List<String> noSeats = new ArrayList<>();
        Set<Long> seen = new HashSet<>();

        Optional<Ride> rideOpt = rideRepo.findById(ticket.getRideId());
        if (rideOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Selected ride not found.");
            return reloadForm(model, principal, session, ticket);
        }
        Ride ride = rideOpt.get();
        int seatLimit = ride.getSeat_limit() != null ? ride.getSeat_limit() : Integer.MAX_VALUE;
        int slotCount = ride.getNo_slots() != null ? ride.getNo_slots() : 0;

        if (ticket.getSlot() < 1 || ticket.getSlot() > slotCount) {
            model.addAttribute("errorMessage", "Invalid slot. Choose between 1 and " + slotCount);
            return reloadForm(model, principal, session, ticket);
        }

        long existing = bookingRepo.countByRideIdAndDateAndSlot(ticket.getRideId(), ticket.getDate(), ticket.getSlot());
        int remainingSeats = Math.max(0, seatLimit - (int) existing);
        model.addAttribute("availabilityMessage",
                "Available seats for slot " + ticket.getSlot() + " on " + ticket.getDate() + ": " + remainingSeats);

        if (ticket.getVisitorIds() == null || ticket.getVisitorIds().isEmpty()) {
            model.addAttribute("errorMessage", "Please select at least one visitor.");
            return reloadForm(model, principal, session, ticket);
        }

        List<Long> visitorList = new ArrayList<>(ticket.getVisitorIds());
        int selectedCount = visitorList.size();

        if (selectedCount > remainingSeats && (partialConfirm == null || !partialConfirm)) {
            model.addAttribute("partialBookingRequired", true);
            model.addAttribute("remainingSeats", remainingSeats);
            model.addAttribute("selectedVisitorsCount", selectedCount);
            model.addAttribute("ticket", ticket);
            return reloadForm(model, principal, session, ticket);
        }

        int seatsToBook = Math.min(remainingSeats, selectedCount);

        for (Long visitorId : visitorList) {
            if (!seen.add(visitorId)) {
                visitorRepo.findById(visitorId.intValue()).ifPresent(v -> duplicateSelected.add(v.getName()));
                continue;
            }

            boolean exists = bookingRepo.existsByVisitorIdAndRideIdAndDateAndSlot(
                    visitorId.intValue(), ticket.getRideId(), ticket.getDate(), ticket.getSlot());
            if (exists) {
                visitorRepo.findById(visitorId.intValue()).ifPresent(v -> alreadyBooked.add(v.getName()));
                continue;
            }

            if (seatsToBook <= 0) {
                visitorRepo.findById(visitorId.intValue()).ifPresent(v -> noSeats.add(v.getName()));
                continue;
            }

            Ride_ticket_master newTicket = new Ride_ticket_master();
            newTicket.setVisitorId(visitorId.intValue());
            newTicket.setRideId(ticket.getRideId());
            newTicket.setDate(ticket.getDate());
            newTicket.setTime(ticket.getTime());
            newTicket.setSlot(ticket.getSlot());
            bookingRepo.save(newTicket);

            seatsToBook--;
            visitorRepo.findById(visitorId.intValue()).ifPresent(v -> {
                newlyBookedNames.add(v.getName());
                Map<String, Object> row = new HashMap<>();
                row.put("id", v.getVisitor_id());
                row.put("name", v.getName());
                newlyBookedForEmail.add(row);
            });
        }

        if (!newlyBookedNames.isEmpty()) {
            model.addAttribute("successMessage", "Tickets booked for: " + String.join(", ", newlyBookedNames));
        }
        if (!alreadyBooked.isEmpty()) {
            model.addAttribute("errorMessage", "Already booked visitors: " + String.join(", ", alreadyBooked));
        }
        if (!duplicateSelected.isEmpty()) {
            model.addAttribute("warningMessage", "Duplicate selection: " + String.join(", ", duplicateSelected));
        }
        if (!noSeats.isEmpty()) {
            model.addAttribute("warningMessageSeats", "Not booked due to no seats: " + String.join(", ", noSeats));
        }

        if (!newlyBookedNames.isEmpty()) {
            session.setAttribute("firstTimeBooking", false);
            session.setAttribute("hasBookedInSession", true);

            try {
                User_master user = userRepo.findByEmail(principal.getName());
                String recipient = user.getEmail() != null ? user.getEmail() : principal.getName();
                mailService.sendRideBookingEmail(
                        recipient,
                        user,
                        ride,
                        ticket.getDate(),
                        ticket.getSlot(),
                        newlyBookedForEmail
                );
            } catch (Exception e) {
                model.addAttribute("warningMessage",
                        "Tickets booked, but we couldn't send the confirmation email right now.");
            }
        }
        return reloadForm(model, principal, session, ticket);
    }
    private String reloadForm(Model model, Principal principal, HttpSession session, Ride_ticket_master ticket) {
        User_master user = userRepo.findByEmail(principal.getName());
        model.addAttribute("ticket", ticket != null ? ticket : new Ride_ticket_master());
        model.addAttribute("rides", rideRepo.findAll());
        model.addAttribute("visitors", visitorRepo.findByUserId(user.getUser_id()));

        Boolean firstTimeBooking = (Boolean) session.getAttribute("firstTimeBooking");
        if (firstTimeBooking == null) firstTimeBooking = true;
        model.addAttribute("firstTimeBooking", firstTimeBooking);

        Boolean hasBooked = (Boolean) session.getAttribute("hasBookedInSession");
        if (hasBooked == null) hasBooked = false;
        model.addAttribute("hasBooked", hasBooked);

        return "Ticket_Booking_Form";
    }
}
