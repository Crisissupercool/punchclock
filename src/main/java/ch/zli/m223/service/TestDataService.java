package ch.zli.m223.service;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Category;
import ch.zli.m223.model.Employee;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.ApplicationUserRepository;
import ch.zli.m223.repository.CategoryRepository;
import ch.zli.m223.repository.EmployeeRepository;
import ch.zli.m223.repository.EntryRepository;
import ch.zli.m223.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Logger;

@ApplicationScoped
public class TestDataService {

    private static final Logger LOG = Logger.getLogger(TestDataService.class.getName());

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    TagRepository tagRepository;

    @Inject
    EntryRepository entryRepository;

    @Inject
    ApplicationUserRepository applicationUserRepository;

    @Inject
    ApplicationUserService applicationUserService;

    @Transactional
    public void loadTestData() {
        LOG.info("Loading test data...");

        // Clear existing data (optional - nur in Dev!)
        clearAllData();

        // 1. Create Employees
        Employee alice = new Employee("Alice", "Schmidt", "alice.schmidt@example.com");
        Employee bob = new Employee("Bob", "Müller", "bob.mueller@example.com");
        Employee charlie = new Employee("Charlie", "Weber", "charlie.weber@example.com");
        Employee diana = new Employee("Diana", "Fischer", "diana.fischer@example.com");

        employeeRepository.persist(alice);
        employeeRepository.persist(bob);
        employeeRepository.persist(charlie);
        employeeRepository.persist(diana);
        LOG.info("Created 4 employees");

        // 2. Create Application Users (with hashed passwords)
        // Each ApplicationUser needs a unique Employee (OneToOne relationship)
        ApplicationUser adminUser = new ApplicationUser("admin", "admin123", "Admin");
        adminUser.setEmployee(alice);
        adminUser = applicationUserService.createUser(adminUser);

        ApplicationUser userBob = new ApplicationUser("bob", "bob123", "User");
        userBob.setEmployee(bob);
        userBob = applicationUserService.createUser(userBob);

        ApplicationUser userCharlie = new ApplicationUser("charlie", "charlie123", "User");
        userCharlie.setEmployee(charlie);
        userCharlie = applicationUserService.createUser(userCharlie);

        LOG.info("Created 3 application users (admin/admin123, bob/bob123, charlie/charlie123)");

        // 3. Create Categories
        Category development = new Category("Development");
        Category meeting = new Category("Meeting");
        Category training = new Category("Training");
        Category support = new Category("Support");
        Category research = new Category("Research");

        categoryRepository.persist(development);
        categoryRepository.persist(meeting);
        categoryRepository.persist(training);
        categoryRepository.persist(support);
        categoryRepository.persist(research);
        LOG.info("Created 5 categories");

        // 3. Create Tags
        Tag urgent = new Tag("Urgent");
        Tag remote = new Tag("Remote");
        Tag onsite = new Tag("Onsite");
        Tag overtime = new Tag("Overtime");
        Tag project = new Tag("Project");
        Tag bugfix = new Tag("Bugfix");
        Tag feature = new Tag("Feature");

        tagRepository.persist(urgent);
        tagRepository.persist(remote);
        tagRepository.persist(onsite);
        tagRepository.persist(overtime);
        tagRepository.persist(project);
        tagRepository.persist(bugfix);
        tagRepository.persist(feature);
        LOG.info("Created 7 tags");

        // 4. Create Entries with relationships
        // Alice's entries
        Entry entry1 = new Entry();
        entry1.setCheckIn(LocalDateTime.now().minusDays(5).withHour(8).withMinute(0));
        entry1.setCheckOut(LocalDateTime.now().minusDays(5).withHour(17).withMinute(30));
        entry1.setEmployee(alice);
        entry1.setCategory(development);
        entry1.setTags(Arrays.asList(remote, project, feature));
        entry1.setDescription("Implemented new authentication feature for web application");
        entryRepository.persist(entry1);

        Entry entry2 = new Entry();
        entry2.setCheckIn(LocalDateTime.now().minusDays(4).withHour(9).withMinute(15));
        entry2.setCheckOut(LocalDateTime.now().minusDays(4).withHour(18).withMinute(0));
        entry2.setEmployee(alice);
        entry2.setCategory(development);
        entry2.setTags(Arrays.asList(onsite, bugfix, urgent));
        entry2.setDescription("Fixed critical bug in payment processing module");
        entryRepository.persist(entry2);

        // Bob's entries
        Entry entry3 = new Entry();
        entry3.setCheckIn(LocalDateTime.now().minusDays(3).withHour(8).withMinute(30));
        entry3.setCheckOut(LocalDateTime.now().minusDays(3).withHour(12).withMinute(30));
        entry3.setEmployee(bob);
        entry3.setCategory(meeting);
        entry3.setTags(Arrays.asList(remote, project));
        entry3.setDescription("Sprint planning meeting with product team");
        entryRepository.persist(entry3);

        Entry entry4 = new Entry();
        entry4.setCheckIn(LocalDateTime.now().minusDays(2).withHour(10).withMinute(0));
        entry4.setCheckOut(LocalDateTime.now().minusDays(2).withHour(19).withMinute(0));
        entry4.setEmployee(bob);
        entry4.setCategory(development);
        entry4.setTags(Arrays.asList(onsite, overtime, urgent));
        entry4.setDescription("Database migration and performance optimization");
        entryRepository.persist(entry4);

        // Charlie's entries
        Entry entry5 = new Entry();
        entry5.setCheckIn(LocalDateTime.now().minusDays(1).withHour(8).withMinute(0));
        entry5.setCheckOut(LocalDateTime.now().minusDays(1).withHour(17).withMinute(0));
        entry5.setEmployee(charlie);
        entry5.setCategory(training);
        entry5.setTags(Arrays.asList(remote));
        entry5.setDescription("Attended Quarkus framework training workshop");
        entryRepository.persist(entry5);

        Entry entry6 = new Entry();
        entry6.setCheckIn(LocalDateTime.now().withHour(7).withMinute(45));
        entry6.setCheckOut(LocalDateTime.now().withHour(16).withMinute(15));
        entry6.setEmployee(charlie);
        entry6.setCategory(support);
        entry6.setTags(Arrays.asList(onsite, urgent));
        entry6.setDescription("Customer support for production deployment issues");
        entryRepository.persist(entry6);

        // Diana's entries
        Entry entry7 = new Entry();
        entry7.setCheckIn(LocalDateTime.now().minusDays(6).withHour(9).withMinute(0));
        entry7.setCheckOut(LocalDateTime.now().minusDays(6).withHour(18).withMinute(30));
        entry7.setEmployee(diana);
        entry7.setCategory(research);
        entry7.setTags(Arrays.asList(remote, project));
        entry7.setDescription("Research on microservices architecture patterns");
        entryRepository.persist(entry7);

        Entry entry8 = new Entry();
        entry8.setCheckIn(LocalDateTime.now().minusDays(5).withHour(8).withMinute(15));
        entry8.setCheckOut(LocalDateTime.now().minusDays(5).withHour(17).withMinute(45));
        entry8.setEmployee(diana);
        entry8.setCategory(development);
        entry8.setTags(Arrays.asList(onsite, feature, project));
        entry8.setDescription("Developed REST API endpoints for mobile app integration");
        entryRepository.persist(entry8);

        // Current ongoing entry (no checkout)
        Entry entry9 = new Entry();
        entry9.setCheckIn(LocalDateTime.now().withHour(8).withMinute(0));
        entry9.setCheckOut(null);
        entry9.setEmployee(alice);
        entry9.setCategory(development);
        entry9.setTags(Arrays.asList(remote, feature));
        entry9.setDescription("Working on user dashboard UI improvements");
        entryRepository.persist(entry9);

        LOG.info("Created 9 entries with relationships");
        LOG.info("Test data loaded successfully!");
    }

    @Transactional
    public void clearAllData() {
        LOG.info("Clearing all existing data...");
        entryRepository.deleteAll();
        applicationUserRepository.deleteAll();
        employeeRepository.deleteAll();
        categoryRepository.deleteAll();
        tagRepository.deleteAll();
        LOG.info("All data cleared");
    }
}
