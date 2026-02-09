package jad.assignment.silvercare.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import jad.assignment.silvercare.model.Event;
import jad.assignment.silvercare.model.EventBooking;
import jad.assignment.silvercare.model.EventDAO;
import jad.assignment.silvercare.service.EmailService;


class EventBookingRequestBody {
  private String guestName;
  private String guestEmail;
  
  public String getGuestName() {
    return guestName;
  }
  public String getGuestEmail() {
    return guestEmail;
  }
}

@RestController
@RequestMapping("/services/events")
public class EventController {
  
  private final EmailService emailService;

  public EventController(EmailService emailService) {
    this.emailService = emailService;
  }
  // GET /services/events
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Object> getAllEvents(@RequestParam(required = false) String search) {
    ArrayList<Event> eventsArr = new ArrayList<>();
    try {
      eventsArr = EventDAO.getAllEvents(search);
      return ResponseEntity.ok(eventsArr);
    } catch (Exception e) {
      System.out.print("Get Events Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Failed to fetch events.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }

  // GET /services/events/{eventId}
  @RequestMapping(path = "/{eventId}", method = RequestMethod.GET)
  public ResponseEntity<Object> getEventById(@PathVariable int eventId) {
    try {
      Event eventBean = EventDAO.getEventById(eventId);
      if (eventBean == null) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Event with ID " + eventId + " not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
      }
      return ResponseEntity.ok(eventBean);
    } catch (Exception e) {
      System.out.print("Get Event By Id Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Internal server error");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }

  // POST /services/events/book/{eventId}
  @RequestMapping(path = "/book/{eventId}", method = RequestMethod.POST)
  public ResponseEntity<Object> bookEvent(@PathVariable int eventId,
      @RequestParam(required = false) Integer clientId,
      @RequestParam(required = false) Integer userId,
      @RequestBody EventBookingRequestBody request) {
    try {
      // verify event exists
      Event ev = EventDAO.getEventById(eventId);
      if (ev == null) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Event not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
      }

      // check capacity
      int current = EventBooking.getBookingCount(eventId);
      if (ev.getCapacity() > 0 && current >= ev.getCapacity()) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Event capacity reached");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
      }

      String guestName = request.getGuestName();
      String guestEmail = request.getGuestEmail();

      // Check duplicate booking
      boolean exists = EventBooking.hasBooking(eventId, clientId, userId, guestEmail);
      if (exists) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Already booked for this event.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
      }

      int bookingId = EventBooking.createBooking(eventId, clientId, userId, guestName, guestEmail);
      if (bookingId <= 0) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Failed to create booking.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
      }

      try {
        if (guestEmail != null && !guestEmail.isBlank()) {
          emailService.sendBookingConfirmation(
              guestEmail,
              guestName,
              eventId,
              bookingId
          );
        }
      } catch (Exception ex) {
        System.out.println("Email failed to send: " + ex);
      }

      java.util.Map<String, Object> resp = new java.util.HashMap<>();
      resp.put("message", "Event booked successfully. For more details, please check your email.");
      resp.put("eventId", eventId);
      resp.put("bookingId", bookingId);
      return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    } catch (Exception e) {
      System.out.print("Book Event Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Failed to book event.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }
}
