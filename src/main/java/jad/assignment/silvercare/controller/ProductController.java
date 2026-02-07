package jad.assignment.silvercare.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jad.assignment.silvercare.mappings.BookServiceRequestBody;
import jad.assignment.silvercare.model.Availability;
import jad.assignment.silvercare.model.AvailabilityDAO;
import jad.assignment.silvercare.model.BookingDAO;
import jad.assignment.silvercare.model.GuestDAO;
import jad.assignment.silvercare.model.Product;
import jad.assignment.silvercare.model.ProductDAO;

@RestController
@RequestMapping("/services")

public class ProductController {
  // GET /services
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Object> getAllProducts(@RequestParam(required = false) String search) {
    ArrayList<Product> productsArr = new ArrayList<>();
    try {
      productsArr = ProductDAO.getAllProducts(search);
      return ResponseEntity.ok(productsArr);
    } catch (Exception e) {
      System.out.print("Get Products Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Failed to fetch products.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }

  // GET /services/{productId}
  @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
  public ResponseEntity<Object> getProductById(@PathVariable int productId) {
    try {
      Product productBean = ProductDAO.getProductById(productId);
      if (productBean == null) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Product with ID " + productId + " not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
      }
      return ResponseEntity.ok(productBean);
    } catch (Exception e) {
      System.out.print("Get Product By Id Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Failed to fetch product.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }

  // GET /services/{productId}/timeslots
  @RequestMapping(path = "/{productId}/timeslots", method = RequestMethod.GET)
  public ResponseEntity<Object> getProductTimeslotsById(@PathVariable int productId) {
    ArrayList<Availability> availabilityArr = new ArrayList<>();
    try {
      availabilityArr = AvailabilityDAO.getAvailabilityByProductId(productId);
      return ResponseEntity.ok(availabilityArr);
    } catch (Exception e) {
      System.out.print("Get Product Timeslots Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Failed to fetch timeslots.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }

  // POST /services/book
  @RequestMapping(path = "/book/{productId}", method = RequestMethod.POST)
  public ResponseEntity<Object> bookService(@PathVariable int productId, @RequestBody BookServiceRequestBody request) {
    int guestId;
    int bookingId;
    try {
      String firstName = request.getFirstName();
      String lastName = request.getLastName();
      Date dob = request.getDob();
      String gender = request.getGender();
      String phone = request.getPhone();
      String email = request.getEmail();

      // normalize phone: remove non-digits and ensure max 8 characters (DB column is char(8))
      String normalizedPhone = (phone == null) ? "" : phone.replaceAll("\\D", "");
      if (normalizedPhone.length() > 8) {
        normalizedPhone = normalizedPhone.substring(normalizedPhone.length() - 8); // keep last 8 digits
      }
      guestId = GuestDAO.createGuest(firstName, lastName, dob, gender, normalizedPhone, email);
      System.out.print("New Guest ID: " + guestId);

      if (guestId == null || guestId <= 0) {
        java.util.Map<String, String> err = new java.util.HashMap<>();
        err.put("error", "Failed to create guest in the database.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
      }

      int caregiverId = request.getCaregiverId();
      String specialRequests = request.getSpecialRequests();
      String bookingTimeslot = request.getBookingTimeslot();

      bookingId = BookingDAO.createGuestBooking(guestId, productId, caregiverId, specialRequests,
          Timestamp.valueOf(bookingTimeslot));
      System.out.print("New Booking ID: " + bookingId);

      java.util.Map<String, Object> resp = new java.util.HashMap<>();
      resp.put("message", "Service booked successfully.");
      resp.put("productId", productId);
      resp.put("bookingId", bookingId);
      resp.put("guestId", guestId);
      return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    } catch (Exception e) {
      System.out.print("Book Service Error:" + e);
      java.util.Map<String, String> err = new java.util.HashMap<>();
      err.put("error", "Failed to book service.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
  }
}
