# Punchclock - Architektur Dokumentation

## Projektstruktur

```
punchclock/
├── src/main/java/ch/zli/m223/
│   ├── controller/           # REST API Endpunkte
│   │   ├── EmployeeController.java
│   │   ├── EntryController.java
│   │   ├── CategoryController.java
│   │   ├── TagController.java
│   │   └── TestDataController.java
│   │
│   ├── service/              # Business Logic Layer
│   │   ├── EmployeeService.java
│   │   ├── EntryService.java
│   │   ├── CategoryService.java
│   │   ├── TagService.java
│   │   └── TestDataService.java
│   │
│   ├── repository/           # Data Access Layer (Panache)
│   │   ├── EmployeeRepository.java
│   │   ├── EntryRepository.java
│   │   ├── CategoryRepository.java
│   │   └── TagRepository.java
│   │
│   ├── model/                # JPA Entitäten
│   │   ├── Employee.java
│   │   ├── Entry.java
│   │   ├── Category.java
│   │   └── Tag.java
│   │
│   ├── dto/                  # Data Transfer Objects
│   │   └── EntryDTO.java
│   │
│   └── ApplicationStartup.java  # Startup Event Listener
│
└── src/main/resources/
    └── application.properties   # Konfiguration
```

---

## Layer-Architektur

```
┌─────────────────────────────────────────────┐
│           REST API Layer                    │
│  (Controller - JAX-RS Endpoints)            │
│  - @Path, @GET, @POST, @PUT, @DELETE        │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│        Service Layer                        │
│  (Business Logic)                           │
│  - @ApplicationScoped                       │
│  - @Transactional                           │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│     Repository Layer                        │
│  (Data Access - Panache)                    │
│  - PanacheRepository                        │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│         Database Layer                      │
│  (PostgreSQL)                               │
│  - Tables: employee, entry, category,       │
│    tag, entry_tag                           │
└─────────────────────────────────────────────┘
```

---

## Komponenten-Beschreibung

### 1. Controller Layer
**Verantwortung:** HTTP Request/Response Handling

**Beispiel: EntryController**
```java
@Path("/entries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntryController {
    @Inject EntryService entryService;

    @GET
    public List<EntryDTO> getAllEntries() { ... }

    @POST
    public Response createEntry(EntryDTO dto) { ... }
}
```

**Features:**
- JAX-RS Annotations
- DTO ↔ Entity Konvertierung
- HTTP Status Codes
- Validierung der Eingaben

---

### 2. Service Layer
**Verantwortung:** Business Logic & Transaktionen

**Beispiel: EntryService**
```java
@ApplicationScoped
public class EntryService {
    @Inject EntryRepository entryRepository;

    @Transactional
    public Entry createEntry(Entry entry) { ... }

    @Transactional
    public Entry updateEntry(Long id, Entry updated) { ... }
}
```

**Features:**
- `@ApplicationScoped` - CDI Bean
- `@Transactional` - Datenbank-Transaktionen
- Business-Logik Validierung
- Entity-Manipulation

---

### 3. Repository Layer
**Verantwortung:** Datenbankzugriff

**Beispiel: EntryRepository**
```java
@ApplicationScoped
public class EntryRepository implements PanacheRepository<Entry> {
    // Panache bietet automatisch:
    // - findById(Long id)
    // - listAll()
    // - persist(Entity)
    // - delete(Entity)
}
```

**Features:**
- Hibernate Panache Pattern
- Automatische CRUD-Operationen
- Type-safe Queries
- Active Record Pattern

---

### 4. Model Layer
**Verantwortung:** Datenmodell & Beziehungen

**Beispiel: Entry**
```java
@Entity
@Table(name = "entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToMany
    @JoinTable(name = "entry_tag", ...)
    private List<Tag> tags;
}
```

**JPA Annotations:**
- `@Entity` - JPA Entität
- `@Table` - Tabellenname
- `@ManyToOne`, `@OneToMany`, `@ManyToMany` - Beziehungen
- `@JsonBackReference`, `@JsonManagedReference` - JSON Serialisierung

---

## Datenbank-Schema

```sql
-- Employee Tabelle
CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- Category Tabelle
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Tag Tabelle
CREATE TABLE tag (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Entry Tabelle
CREATE TABLE entry (
    id BIGSERIAL PRIMARY KEY,
    check_in TIMESTAMP NOT NULL,
    check_out TIMESTAMP,
    employee_id BIGINT NOT NULL REFERENCES employee(id),
    category_id BIGINT REFERENCES category(id)
);

-- Entry-Tag Join Tabelle (ManyToMany)
CREATE TABLE entry_tag (
    entry_id BIGINT REFERENCES entry(id),
    tag_id BIGINT REFERENCES tag(id),
    PRIMARY KEY (entry_id, tag_id)
);
```

---

## Technologie-Stack

### Backend Framework
- **Quarkus 3.30.2** - Cloud-native Java Framework
- **Java 21** - Programming Language

### Persistence
- **Hibernate ORM** - Object-Relational Mapping
- **Panache** - Simplified Hibernate
- **PostgreSQL 16** - Relationale Datenbank

### API
- **JAX-RS (RESTEasy)** - REST API
- **Jackson** - JSON Serialisierung

### Dependency Injection
- **CDI (Contexts and Dependency Injection)** - Jakarta EE Standard

### Build Tool
- **Maven** - Projekt Management

### Container
- **Docker & Docker Compose** - Containerisierung

---

## Design Patterns

### 1. Repository Pattern
Trennung von Business Logic und Datenzugriff
```java
Repository → Service → Controller
```

### 2. DTO Pattern
Vermeidung von zirkulären Referenzen und Kontrolle über API-Response
```java
Entity → DTO (im Controller)
DTO → Entity (im Controller)
```

### 3. Dependency Injection
Lose Kopplung durch CDI
```java
@Inject
EntryService entryService;
```

### 4. Active Record (Panache)
Vereinfachter Datenzugriff
```java
repository.persist(entity);
repository.findById(id);
```

---

## Konfiguration

### application.properties
```properties
# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://db:5432/app
quarkus.datasource.username=app
quarkus.datasource.password=app

# Hibernate
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true

# HTTP
quarkus.http.host=0.0.0.0

# Custom
app.testdata.load-on-startup=true
```

---

## Datenfluss Beispiel: Entry erstellen

```
1. HTTP POST /entries
   └─> EntryController.createEntry(EntryDTO)

2. Validierung & Konvertierung
   ├─> Employee aus EmployeeService laden
   ├─> Category aus CategoryService laden (optional)
   ├─> Tags aus TagService laden (optional)
   └─> DTO → Entry Entity konvertieren

3. Service Layer
   └─> EntryService.createEntry(Entry)
       └─> @Transactional Start

4. Repository Layer
   └─> EntryRepository.persist(Entry)
       └─> Hibernate generiert SQL INSERT

5. Database
   └─> PostgreSQL führt INSERT aus
       └─> Gibt generierte ID zurück

6. Response
   └─> Entry → EntryDTO konvertieren
       └─> HTTP 201 Created + JSON Response
```

---

## Erweiterbarkeit

### Neue Entität hinzufügen

1. **Model** erstellen (`@Entity`)
2. **Repository** erstellen (implements `PanacheRepository`)
3. **Service** erstellen (`@ApplicationScoped`, `@Transactional`)
4. **Controller** erstellen (`@Path`, REST-Methoden)
5. Optional: **DTO** erstellen

### Beispiel: Neue Entität "Project"
```java
// 1. Model
@Entity
public class Project { ... }

// 2. Repository
@ApplicationScoped
public class ProjectRepository implements PanacheRepository<Project> { }

// 3. Service
@ApplicationScoped
public class ProjectService { ... }

// 4. Controller
@Path("/projects")
public class ProjectController { ... }
```

---

## Best Practices

✅ **DO:**
- DTOs für API-Responses verwenden
- Transaktionen im Service Layer
- Validierung in Controller & Service
- Klare Trennung der Layer
- Dependency Injection nutzen

❌ **DON'T:**
- Entitäten direkt in API zurückgeben (zirkuläre Referenzen!)
- Business Logic im Controller
- Transaktionen im Repository
- Tight Coupling zwischen Layern

---

## Sicherheit & Produktion

### Für Produktion anpassen:

```properties
# Keine automatische Schema-Generierung!
quarkus.hibernate-orm.database.generation=none

# Testdaten deaktivieren
app.testdata.load-on-startup=false

# SQL Logging deaktivieren
quarkus.hibernate-orm.log.sql=false
```

### Empfohlene Erweiterungen:
- **Authentication & Authorization** (JWT)
- **Validation** (Bean Validation)
- **Error Handling** (Exception Mapper)
- **API Documentation** (OpenAPI/Swagger)
- **Logging** (Structured Logging)
- **Monitoring** (Health Checks, Metrics)
