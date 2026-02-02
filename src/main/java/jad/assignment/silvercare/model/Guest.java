package jad.assignment.silvercare.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Guest {
  private int guestId;
	private String firstName;
	private String lastName;
	private Date dob;
	private String gender;
	private String phone;
	private String email;
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
