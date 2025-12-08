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
}

