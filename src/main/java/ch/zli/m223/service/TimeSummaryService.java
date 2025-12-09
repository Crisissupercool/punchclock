package ch.zli.m223.service;

import ch.zli.m223.dto.TimeSummaryDTO;
import ch.zli.m223.model.Entry;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class TimeSummaryService {

    /**
     * Calculate time summary per day from a list of entries.
     *
     * @param entries List of Entry objects
     * @return List of TimeSummaryDTO with date and total duration per day
     */
    public List<TimeSummaryDTO> calculateSummaryPerDay(List<Entry> entries) {
        // Filter out entries without checkOut (ongoing entries)
        List<Entry> completedEntries = entries.stream()
                .filter(entry -> entry.getCheckOut() != null)
                .collect(Collectors.toList());

        // Group entries by date and calculate total duration per day
        Map<LocalDate, Duration> summaryMap = completedEntries.stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getCheckIn().toLocalDate(),
                        Collectors.reducing(
                                Duration.ZERO,
                                entry -> Duration.between(entry.getCheckIn(), entry.getCheckOut()),
                                Duration::plus
                        )
                ));

        // Convert map to list of DTOs and sort by date
        List<TimeSummaryDTO> summaries = new ArrayList<>();
        summaryMap.forEach((date, duration) -> {
            summaries.add(new TimeSummaryDTO(date, duration));
        });

        // Sort by date (newest first)
        summaries.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return summaries;
    }
}
