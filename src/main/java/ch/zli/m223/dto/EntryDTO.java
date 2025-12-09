package ch.zli.m223.dto;

import java.time.LocalDateTime;

public class EntryDTO {
    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long employeeId; // statt komplettes Employee-Objekt

    public EntryDTO() {}

    public EntryDTO(Long id, LocalDateTime checkIn, LocalDateTime checkOut, Long employeeId) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.employeeId = employeeId;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    
}
