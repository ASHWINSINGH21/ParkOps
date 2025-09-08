package com.example.Park.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "feedback")
@Entity
public class Feedback1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int feedback_id;

    int visitor_id, ride_id;
    private Integer rating;
    String comment, response;
    boolean resolved = false;
    LocalDate submitted_at;

    public String getResponse() {
        return response;
    }

    public Feedback1(String response, boolean resolved) {
        this.response = response;
        this.resolved = resolved;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDate getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(LocalDate submitted_at) {
        this.submitted_at = submitted_at;
    }

    public Feedback1() {
    }

    public Feedback1(int feedback_id, int visitor_id, int ride_id, Integer rating, String comment) {
        this.feedback_id = feedback_id;
        this.visitor_id = visitor_id;
        this.ride_id = ride_id;
        this.rating = rating;
        this.comment = comment;
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public int getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(int visitor_id) {
        this.visitor_id = visitor_id;
    }

    public int getRide_id() {
        return ride_id;
    }

    public void setRide_id(int ride_id) {
        this.ride_id = ride_id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
