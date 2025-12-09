package ch.zli.m223.dto;

import java.time.LocalDate;
import java.time.Duration;

public class TimeSummaryDTO {
    private LocalDate date;
    private Duration duration;

    public TimeSummaryDTO() {
    }

    public TimeSummaryDTO(LocalDate date, Duration duration) {
        this.date = date;
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    // Helper method to format duration as HH:mm:ss
    public String getFormattedDuration() {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
