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

import jad.assignment.silvercare.mappings.BookServiceRequestBody;
import jad.assignment.silvercare.model.BookingDAO;
import jad.assignment.silvercare.model.GuestDAO;
import jad.assignment.silvercare.model.Product;
import jad.assignment.silvercare.model.ProductDAO;

@RestController
@RequestMapping("/services")

public class ProductController {
  // GET /services
  @RequestMapping(method = RequestMethod.GET)
  public ArrayList<Product> getAllProducts(@RequestParam(required = false) String search) {
    ArrayList<Product> productsArr = new ArrayList<>();
    try {
      productsArr = ProductDAO.getAllProducts(search);
    } catch (Exception e) {
      System.out.print("Get Products Error:" + e);
    }

    return productsArr;
  }

  // GET /services/{productId}
  @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
  public Product getProductById(@PathVariable int productId) {
    Product productBean = null;
    try {
      productBean = ProductDAO.getProductById(productId);
    } catch (Exception e) {
      System.out.print("Get Product By Id Error:" + e);
    }

    return productBean;
  }

  // POST /services/book
  @RequestMapping(path = "/book/{productId}", method = RequestMethod.POST)
  public String bookService(@PathVariable int productId, @RequestBody BookServiceRequestBody request) {
    int guestId;
    int bookingId;
    try {
      String firstName = request.getFirstName();
      String lastName = request.getLastName();
      Date dob = request.getDob();
      String gender = request.getGender();
      String phone = request.getPhone();
      String email = request.getEmail();

      guestId = GuestDAO.createGuest(firstName, lastName, dob, gender, phone, email);
      System.out.print("New Guest ID: " + guestId);

      int caregiverId = request.getCaregiverId();
      String specialRequests = request.getSpecialRequests();
      String bookingTimeslot = request.getBookingTimeslot();

      bookingId = BookingDAO.createGuestBooking(guestId, productId, caregiverId, specialRequests,
          Timestamp.valueOf(bookingTimeslot));
      System.out.print("New Booking ID: " + bookingId);

      return "Service with Product ID " + productId + " booked successfully.";
    } catch (Exception e) {
      System.out.print("Book Service Error:" + e);
      return "Failed to book service.";
    }
  }
}
