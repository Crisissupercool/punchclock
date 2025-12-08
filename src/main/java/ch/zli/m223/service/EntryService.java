package ch.zli.m223.service;

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

    public List<Entry> findAll() {
        return entryRepository.listAll();
    }

    public Entry findById(Long id) {
        return entryRepository.findById(id);
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

