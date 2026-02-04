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
  // GET /services/events
  @RequestMapping(method = RequestMethod.GET)
  public ArrayList<Event> getAllEvents(@RequestParam(required = false) String search) {
    ArrayList<Event> eventsArr = new ArrayList<>();
    try {
      eventsArr = EventDAO.getAllEvents(search);
    } catch (Exception e) {
      System.out.print("Get Events Error:" + e);
    }

    return eventsArr;
  }

  // GET /services/events/{eventId}
  @RequestMapping(path = "/{eventId}", method = RequestMethod.GET)
  public Event getEventById(@PathVariable int eventId) {
    Event eventBean = null;
    try {
      eventBean = EventDAO.getEventById(eventId);
    } catch (Exception e) {
      System.out.print("Get Event By Id Error:" + e);
    }

    return eventBean;
  }

  // POST /services/events/book/{eventId}
  @RequestMapping(path = "/book/{eventId}", method = RequestMethod.POST)
  public ResponseEntity<String> bookEvent(@PathVariable int eventId,
      @RequestParam(required = false) Integer clientId,
      @RequestParam(required = false) Integer userId,
      @RequestBody EventBookingRequestBody request) {
    try {
      // verify event exists
      Event ev = EventDAO.getEventById(eventId);
      if (ev == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
      }

      // check capacity
      int current = EventBooking.getBookingCount(eventId);
      if (ev.getCapacity() > 0 && current >= ev.getCapacity()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Event capacity reached");
      }

      String guestName = request.getGuestName();
      String guestEmail = request.getGuestEmail();

      // Check duplicate booking
      boolean exists = EventBooking.hasBooking(eventId, clientId, userId, guestEmail);
      if (exists) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Already booked for this event.");
      }

      int bookingId = EventBooking.createBooking(eventId, clientId, userId, guestName, guestEmail);
      if (bookingId <= 0) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create booking.");
      }

      return ResponseEntity.status(HttpStatus.CREATED).body("Event with ID " + eventId + " booked successfully. Booking ID: " + bookingId);
    } catch (Exception e) {
      System.out.print("Book Event Error:" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book event.");
    }
  }
}
