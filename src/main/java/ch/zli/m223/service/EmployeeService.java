package ch.zli.m223.service;

import ch.zli.m223.model.Employee;
import ch.zli.m223.repository.EmployeeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.listAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        employeeRepository.persist(employee);
        return employee;
    }

    // ---------------------------
    // Fehlende Methoden hinzufügen
    // ---------------------------

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee != null) {
            employeeRepository.delete(employee);
        }
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id);

        if (employee == null) {
            return null;
        }

        // Felder updaten
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        // weitere Felder falls vorhanden …

        employeeRepository.persist(employee);
        return employee;
    }
}
