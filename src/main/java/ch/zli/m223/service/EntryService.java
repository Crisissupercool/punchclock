package ch.zli.m223.service;

import ch.zli.m223.dto.TimeSummaryDTO;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Employee;
import ch.zli.m223.repository.EntryRepository;
import ch.zli.m223.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EntryService {

    @Inject
    EntryRepository entryRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    TimeSummaryService timeSummaryService;

    public List<Entry> findAll() {
        return entryRepository.listAll();
    }

    public List<TimeSummaryDTO> getTimeSummaries() {
        List<Entry> allEntries = entryRepository.listAll();
        return timeSummaryService.calculateSummaryPerDay(allEntries);
    }

    public Entry findById(Long id) {
        return entryRepository.findById(id);
    }

    @Transactional
    public void deleteEntry(Long id) {
        Entry entry = entryRepository.findById(id);
        if (entry != null) {
            entryRepository.delete(entry);
        }
    }

    @Transactional
    public Entry updateEntry(Long id, Entry updatedEntry) {
        Entry entry = entryRepository.findById(id);
        if (entry == null) {
            return null;
        }

        entry.setCheckIn(updatedEntry.getCheckIn());
        entry.setCheckOut(updatedEntry.getCheckOut());
        entry.setEmployee(updatedEntry.getEmployee());
        entry.setCategory(updatedEntry.getCategory());
        entry.setTags(updatedEntry.getTags());
        return entry; // Panache updated automatically inside the transaction
    }

    @Transactional
    public Entry createEntry(Entry entry) {
        if (entry.getEmployee() != null && entry.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(entry.getEmployee().getId());
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found");
            }
            entry.setEmployee(employee);
        }
        entryRepository.persist(entry);
        return entry;
    }
}
