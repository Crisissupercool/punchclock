# Punchclock REST API Dokumentation

## Übersicht

Die Punchclock-Anwendung bietet eine vollständige REST API zur Verwaltung von Mitarbeitern, Zeiteinträgen, Kategorien und Tags.

**Base URL:** `http://localhost:8080`

---

## Entitäten

### 1. Employee (Mitarbeiter)
Repräsentiert einen Mitarbeiter im System.

**Felder:**
- `id` (Long) - Eindeutige ID
- `firstName` (String) - Vorname
- `lastName` (String) - Nachname
- `email` (String) - E-Mail (unique)
- `entries` (List<Entry>) - Zugehörige Zeiteinträge

### 2. Entry (Zeiteintrag)
Repräsentiert einen Check-in/Check-out Eintrag.

**Felder:**
- `id` (Long) - Eindeutige ID
- `checkIn` (LocalDateTime) - Check-in Zeit
- `checkOut` (LocalDateTime) - Check-out Zeit (optional)
- `employee` (Employee) - Zugehöriger Mitarbeiter
- `category` (Category) - Kategorie (optional)
- `tags` (List<Tag>) - Zugehörige Tags

### 3. Category (Kategorie)
Kategorisiert Zeiteinträge (z.B. Development, Meeting).

**Felder:**
- `id` (Long) - Eindeutige ID
- `title` (String) - Kategorie-Titel
- `entries` (List<Entry>) - Zugehörige Einträge

### 4. Tag
Tags für Zeiteinträge (z.B. Urgent, Remote, Onsite).

**Felder:**
- `id` (Long) - Eindeutige ID
- `title` (String) - Tag-Titel
- `entries` (List<Entry>) - Zugehörige Einträge

### 5. EntryDTO (Data Transfer Object)
Vereinfachte Darstellung für API-Kommunikation.

**Felder:**
- `id` (Long)
- `checkIn` (LocalDateTime)
- `checkOut` (LocalDateTime)
- `employeeId` (Long)
- `categoryId` (Long)
- `tagIds` (List<Long>)

---

## API Endpunkte

### Employee Endpoints

#### Alle Mitarbeiter abrufen
```http
GET /employees
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "firstName": "Alice",
    "lastName": "Schmidt",
    "email": "alice.schmidt@example.com"
  }
]
```

#### Einzelnen Mitarbeiter abrufen
```http
GET /employees/{id}
```

**Response:** `200 OK` oder `404 Not Found`
```json
{
  "id": 1,
  "firstName": "Alice",
  "lastName": "Schmidt",
  "email": "alice.schmidt@example.com"
}
```

#### Mitarbeiter erstellen
```http
POST /employees
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "Max",
  "lastName": "Mustermann",
  "email": "max.mustermann@example.com"
}
```

**Response:** `201 Created`

#### Mitarbeiter aktualisieren
```http
PUT /employees/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "Max",
  "lastName": "Musterfrau",
  "email": "max.musterfrau@example.com"
}
```

**Response:** `200 OK` oder `404 Not Found`

#### Mitarbeiter löschen
```http
DELETE /employees/{id}
```

**Response:** `204 No Content`

---

### Category Endpoints

#### Alle Kategorien abrufen
```http
GET /categories
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Development"
  }
]
```

#### Einzelne Kategorie abrufen
```http
GET /categories/{id}
```

**Response:** `200 OK` oder `404 Not Found`

#### Kategorie erstellen
```http
POST /categories
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Planning"
}
```

**Response:** `201 Created`

#### Kategorie aktualisieren
```http
PUT /categories/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Project Planning"
}
```

**Response:** `200 OK` oder `404 Not Found`

#### Kategorie löschen
```http
DELETE /categories/{id}
```

**Response:** `204 No Content`

---

### Tag Endpoints

#### Alle Tags abrufen
```http
GET /tags
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Urgent"
  }
]
```

#### Einzelnen Tag abrufen
```http
GET /tags/{id}
```

**Response:** `200 OK` oder `404 Not Found`

#### Tag erstellen
```http
POST /tags
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Priority"
}
```

**Response:** `201 Created`

#### Tag aktualisieren
```http
PUT /tags/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "High Priority"
}
```

**Response:** `200 OK` oder `404 Not Found`

#### Tag löschen
```http
DELETE /tags/{id}
```

**Response:** `204 No Content`

---

### Entry Endpoints

#### Alle Einträge abrufen
```http
GET /entries
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "checkIn": "2025-12-09T08:00:00",
    "checkOut": "2025-12-09T17:00:00",
    "employeeId": 1,
    "categoryId": 1,
    "tagIds": [1, 2, 3]
  }
]
```

#### Einzelnen Eintrag abrufen
```http
GET /entries/{id}
```

**Response:** `200 OK` oder `404 Not Found`

#### Eintrag erstellen
```http
POST /entries
Content-Type: application/json
```

**Request Body:**
```json
{
  "checkIn": "2025-12-09T08:00:00",
  "checkOut": "2025-12-09T17:00:00",
  "employeeId": 1,
  "categoryId": 2,
  "tagIds": [1, 3]
}
```

**Hinweise:**
- `checkOut` ist optional (für laufende Einträge)
- `categoryId` ist optional
- `tagIds` ist optional (kann leer sein oder null)

**Response:** `201 Created`

**Fehler:** `400 Bad Request` wenn Employee oder Category nicht existiert

#### Eintrag aktualisieren
```http
PUT /entries/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "checkIn": "2025-12-09T08:15:00",
  "checkOut": "2025-12-09T17:30:00",
  "employeeId": 1,
  "categoryId": 2,
  "tagIds": [2, 4]
}
```

**Response:** `200 OK` oder `404 Not Found`

#### Eintrag löschen
```http
DELETE /entries/{id}
```

**Response:** `204 No Content`

---

### Statistics Endpoints

#### Zeiterfassung pro Tag abrufen
```http
GET /statistics/time-summaries
```

**Response:** `200 OK`
```json
{
  "2024-08-23": "08:00:00",
  "2024-08-22": "09:15:00",
  "2024-08-21": "07:45:00",
  "2024-08-20": "08:30:00"
}
```

**Beschreibung:**
- Gibt eine Zusammenfassung der erfassten Arbeitszeit pro Tag zurück
- Schlüssel: Datum im Format `YYYY-MM-DD`
- Wert: Gesamtdauer im Format `HH:mm:ss`
- Nur abgeschlossene Einträge (mit checkOut) werden berücksichtigt
- Sortiert nach Datum (neueste zuerst)

---

### Test Data Endpoints

#### Testdaten laden
```http
POST /api/testdata
```

**Response:** `200 OK`
```json
{
  "message": "Test data loaded successfully",
  "status": "success"
}
```

**Hinweis:** Löscht alle existierenden Daten und lädt vordefinierte Testdaten.

#### Alle Daten löschen
```http
DELETE /api/testdata
```

**Response:** `200 OK`
```json
{
  "message": "All data cleared successfully",
  "status": "success"
}
```

---

## Beziehungen zwischen Entitäten

### Employee → Entry (1:n)
- Ein Employee kann mehrere Entries haben
- Bidirektionale Beziehung mit `@OneToMany` / `@ManyToOne`
- Beim Löschen eines Employees werden alle zugehörigen Entries gelöscht (cascade)

### Category → Entry (1:n)
- Eine Category kann mehrere Entries haben
- Ein Entry kann optional einer Category zugeordnet sein
- Bidirektionale Beziehung

### Entry ↔ Tag (n:m)
- Ein Entry kann mehrere Tags haben
- Ein Tag kann bei mehreren Entries verwendet werden
- ManyToMany Beziehung mit Join-Table `entry_tag`

---

## Beispiel-Workflows

### 1. Neuen Mitarbeiter mit Eintrag erstellen

```bash
# 1. Mitarbeiter erstellen
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com"
  }'

# Response: {"id": 5, ...}

# 2. Kategorie erstellen
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"title": "Development"}'

# Response: {"id": 6, ...}

# 3. Tags erstellen
curl -X POST http://localhost:8080/tags \
  -H "Content-Type: application/json" \
  -d '{"title": "Remote"}'

# 4. Eintrag erstellen
curl -X POST http://localhost:8080/entries \
  -H "Content-Type: application/json" \
  -d '{
    "checkIn": "2025-12-09T09:00:00",
    "checkOut": "2025-12-09T18:00:00",
    "employeeId": 5,
    "categoryId": 6,
    "tagIds": [8]
  }'
```

### 2. Check-in ohne Check-out (laufender Eintrag)

```bash
curl -X POST http://localhost:8080/entries \
  -H "Content-Type: application/json" \
  -d '{
    "checkIn": "2025-12-09T08:00:00",
    "employeeId": 1,
    "categoryId": 1,
    "tagIds": [1, 2]
  }'
```

### 3. Check-out nachtragen

```bash
curl -X PUT http://localhost:8080/entries/9 \
  -H "Content-Type: application/json" \
  -d '{
    "checkIn": "2025-12-09T08:00:00",
    "checkOut": "2025-12-09T17:00:00",
    "employeeId": 1,
    "categoryId": 1,
    "tagIds": [1, 2]
  }'
```

---

## Status Codes

| Code | Bedeutung |
|------|-----------|
| 200 | OK - Anfrage erfolgreich |
| 201 | Created - Ressource erstellt |
| 204 | No Content - Erfolgreich gelöscht |
| 400 | Bad Request - Ungültige Anfrage |
| 404 | Not Found - Ressource nicht gefunden |
| 500 | Internal Server Error - Serverfehler |

---

## Validierungen

### Employee
- `email` muss unique sein
- `firstName`, `lastName`, `email` sind Pflichtfelder

### Entry
- `checkIn` ist Pflichtfeld
- `employeeId` ist Pflichtfeld und muss existieren
- `categoryId` (falls angegeben) muss existieren
- Alle `tagIds` müssen existieren

### Category / Tag
- `title` ist Pflichtfeld

---

## Technische Details

**Framework:** Quarkus 3.30.2
**Datenbank:** PostgreSQL
**ORM:** Hibernate with Panache
**Serialisierung:** Jackson
**API-Style:** RESTful

**Datenbankschema:**
- Automatische Generierung via `quarkus.hibernate-orm.database.generation=drop-and-create`
- Join-Table für ManyToMany: `entry_tag`
