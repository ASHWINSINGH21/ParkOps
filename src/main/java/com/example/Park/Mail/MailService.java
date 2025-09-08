package com.example.Park.Mail;

import com.example.Park.Ride.Ride;
import com.example.Park.User.User_master;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.mail.from:no-reply@parkscape.example}")
    private String from;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRideBookingEmail(
            String to,
            User_master user,
            Ride ride,
            LocalDate date,
            Integer slot,
            List<Map<String, Object>> visitors
    ) {
        Context ctx = new Context();
        ctx.setVariable("user", user);
        ctx.setVariable("ride", ride);
        ctx.setVariable("date", date);
        ctx.setVariable("slot", slot);
        ctx.setVariable("visitors", visitors);
        ctx.setVariable("totalTickets", visitors.size());
        ctx.setVariable("baseUrl", baseUrl);

        Object unitPrice = null;
        try {
            var m = ride.getClass().getMethod("getTicket_price");
            unitPrice = m.invoke(ride);
        } catch (Exception ignored) {}
        ctx.setVariable("unitPrice", unitPrice);

        String html = templateEngine.process("ride_booking_confirmation", ctx);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Your ParkScape ride tickets");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
