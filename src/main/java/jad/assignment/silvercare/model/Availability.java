package jad.assignment.silvercare.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Availability {
  private int availabilityId;
  private int caregiverId;
  private Date availabilityDate;
  private Time startTime;
  private Time endTime;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public int getAvailabilityId() {
    return availabilityId;
  }

  public void setAvailabilityId(int availabilityId) {
    this.availabilityId = availabilityId;
  }

  public int getCaregiverId() {
    return caregiverId;
  }

  public void setCaregiverId(int caregiverId) {
    this.caregiverId = caregiverId;
  }

  public Date getAvailabilityDate() {
    return availabilityDate;
  }

  public void setAvailabilityDate(Date availabilityDate) {
    this.availabilityDate = availabilityDate;
  }

  public Time getStartTime() {
    return startTime;
  }

  public void setStartTime(Time startTime) {
    this.startTime = startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public void setEndTime(Time endTime) {
    this.endTime = endTime;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }

}
