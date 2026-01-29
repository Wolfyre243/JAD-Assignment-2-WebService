package jad.assignment.silvercare.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jad.assignment.silvercare.model.Event;
import jad.assignment.silvercare.model.EventDAO;


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
}
