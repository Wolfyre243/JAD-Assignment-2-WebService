package jad.assignment.silvercare.service;

// Imports Spring’s simple email message abstraction
import org.springframework.mail.SimpleMailMessage;

// Imports the mail sender interface used to actually send emails
import org.springframework.mail.javamail.JavaMailSender;

// Marks this class as a Spring-managed service component
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  // JavaMailSender is injected by Spring and handles email delivery
  private final JavaMailSender mailSender;

  // Constructor injection ensures mailSender is provided by Spring at runtime
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * Sends a booking confirmation email to a user.
   *
   * @param toEmail   Recipient’s email address
   * @param guestName Name of the guest (may be null or blank)
   * @param eventId   ID of the booked event
   * @param bookingId ID of the booking record
   */
  public void sendBookingConfirmation(
      String toEmail,
      String guestName,
      int eventId,
      int bookingId
  ) {
    // Create a simple plain-text email message
    SimpleMailMessage msg = new SimpleMailMessage();

    // Set the recipient email address
    msg.setTo(toEmail);

    // Set the subject line of the email
    msg.setSubject("ABC Community Club — Event Booking Confirmation");

    // Fallback to "Guest" if guestName is null or empty
    String safeName = (guestName == null || guestName.isBlank()) ? "Guest" : guestName;

    // Build the email body using booking and event details
    msg.setText(
      "Hi " + safeName + ",\n\n" +
      "Your booking is confirmed.\n\n" +
      "Event ID: " + eventId + "\n" +
      "Booking ID: " + bookingId + "\n\n" +
      "Thank you,\n" +
      "ABC Community Club"
    );

    // Send the email using the configured mail sender
    mailSender.send(msg);
  }
}
