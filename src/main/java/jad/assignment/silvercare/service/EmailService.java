package jad.assignment.silvercare.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

      private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendBookingConfirmation(
      String toEmail,
      String guestName,
      int eventId,
      int bookingId
  ) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(toEmail);
    msg.setSubject("ABC Community Club — Event Booking Confirmation");

    String safeName = (guestName == null || guestName.isBlank()) ? "Guest" : guestName;

    msg.setText(
      "Hi " + safeName + ",\n\n" +
      "Your booking is confirmed.\n\n" +
      "Event ID: " + eventId + "\n" +
      "Booking ID: " + bookingId + "\n\n" +
      "Thank you,\n" +
      "ABC Community Club"
    );

    mailSender.send(msg);
  }
}
