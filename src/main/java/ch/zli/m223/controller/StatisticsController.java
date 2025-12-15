package ch.zli.m223.controller;

import ch.zli.m223.dto.TimeSummaryDTO;
import ch.zli.m223.service.EntryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"User", "Admin"})
public class StatisticsController {

    @Inject
    EntryService entryService;

    /**
     * GET /statistics/time-summaries
     *
     * Returns a map with dates as keys and formatted durations as values.
     * Example:
     * {
     *   "2024-08-23": "08:00:00",
     *   "2024-08-22": "09:15:00",
     *   "2024-08-21": "07:45:00",
     *   "2024-08-20": "08:30:00"
     * }
     */
    @GET
    @Path("/time-summaries")
    public Map<String, String> getTimeSummaries() {
        List<TimeSummaryDTO> summaries = entryService.getTimeSummaries();

        // Convert to Map with date as key and formatted duration as value
        Map<String, String> result = new LinkedHashMap<>();
        for (TimeSummaryDTO summary : summaries) {
            result.put(summary.getDate().toString(), summary.getFormattedDuration());
        }

        return result;
    }
}
