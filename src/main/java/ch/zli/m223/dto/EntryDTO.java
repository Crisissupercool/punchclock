package ch.zli.m223.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EntryDTO {
    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long employeeId;
    private Long categoryId;
    private List<Long> tagIds;

    public EntryDTO() {}

    public EntryDTO(Long id, LocalDateTime checkIn, LocalDateTime checkOut, Long employeeId, Long categoryId, List<Long> tagIds) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.employeeId = employeeId;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
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

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<Long> getTagIds() { return tagIds; }
    public void setTagIds(List<Long> tagIds) { this.tagIds = tagIds; }
}
