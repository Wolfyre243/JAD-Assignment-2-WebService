package jad.assignment.silvercare.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jad.assignment.silvercare.model.Event;
import jad.assignment.silvercare.model.EventBookingDAO;
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

  // POST /services/events/book
  @RequestMapping(path = "/book/{eventId}", method = RequestMethod.POST)
  public String bookEvent(@PathVariable int eventId, @RequestBody EventBookingRequestBody request) {
    try {
      String guestName = request.getGuestName();
      String guestEmail = request.getGuestEmail();
      Integer bookingId = EventBookingDAO.createEventBooking(eventId, guestName, guestEmail);

      return "Event with ID " + eventId + " booked successfully. Booking ID: " + bookingId;
    } catch (Exception e) {
      System.out.print("Book Event Error:" + e);
      return "Failed to book event.";
    }
  }
}
