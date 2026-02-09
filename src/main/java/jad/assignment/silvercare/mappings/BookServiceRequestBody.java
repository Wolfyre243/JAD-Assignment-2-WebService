package jad.assignment.silvercare.mappings;

import java.sql.Date;

public class  BookServiceRequestBody {
  // User details
  private String firstName;
	private String lastName;
	private Date dob;
	private String gender;
	private String phone;
	private String email;
  // Booking details
  private String caregiverId;
  private String specialRequests;
  private String bookingTimeslot;

	// Getters
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDob() {
		return dob;
	}

	public String getGender() {
		return gender;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getCaregiverId() {
		return caregiverId;
	}

	public String getSpecialRequests() {
		return specialRequests;
	}

	public String getBookingTimeslot() {
		return bookingTimeslot;
	}
}
