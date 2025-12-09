# Test Data Documentation

## Übersicht

Das Punchclock-Projekt bietet mehrere Möglichkeiten, Testdaten zu laden, um die Entwicklung und Tests zu erleichtern.

## Methoden zum Laden von Testdaten

### 1. Automatisches Laden beim Start (EMPFOHLEN)

Testdaten werden automatisch beim Start der Applikation geladen.

**Konfiguration:** In `application.properties`
```properties
app.testdata.load-on-startup=true
```

**Vorteile:**
- Testdaten sind sofort verfügbar
- Keine manuelle Aktion erforderlich
- Ideal für Entwicklung

**Deaktivieren:** Setze den Wert auf `false`

### 2. Manuelles Laden via REST API

Testdaten können jederzeit manuell geladen werden.

**Testdaten laden:**
```bash
curl -X POST http://localhost:8080/api/testdata
```

**Alle Daten löschen:**
```bash
curl -X DELETE http://localhost:8080/api/testdata
```

**Vorteile:**
- Volle Kontrolle über den Zeitpunkt
- Kann mehrmals ausgeführt werden
- Nützlich für Tests und Demos

## Geladene Testdaten

### Employees (4)
- Alice Schmidt (alice.schmidt@example.com)
- Bob Müller (bob.mueller@example.com)
- Charlie Weber (charlie.weber@example.com)
- Diana Fischer (diana.fischer@example.com)

### Categories (5)
- Development
- Meeting
- Training
- Support
- Research

### Tags (7)
- Urgent
- Remote
- Onsite
- Overtime
- Project
- Bugfix
- Feature

### Entries (9)
Diverse Einträge über mehrere Tage verteilt mit:
- Verschiedenen Mitarbeitern
- Unterschiedlichen Kategorien
- Mehreren Tags pro Eintrag
- Ein laufender Eintrag (ohne Check-out)

## API Endpunkte zum Testen

Nach dem Laden der Testdaten können Sie folgende Endpunkte testen:

### Employees
```bash
GET    http://localhost:8080/employees
GET    http://localhost:8080/employees/1
POST   http://localhost:8080/employees
PUT    http://localhost:8080/employees/1
DELETE http://localhost:8080/employees/1
```

### Categories
```bash
GET    http://localhost:8080/categories
GET    http://localhost:8080/categories/1
POST   http://localhost:8080/categories
PUT    http://localhost:8080/categories/1
DELETE http://localhost:8080/categories/1
```

### Tags
```bash
GET    http://localhost:8080/tags
GET    http://localhost:8080/tags/1
POST   http://localhost:8080/tags
PUT    http://localhost:8080/tags/1
DELETE http://localhost:8080/tags/1
```

### Entries
```bash
GET    http://localhost:8080/entries
GET    http://localhost:8080/entries/1
POST   http://localhost:8080/entries
PUT    http://localhost:8080/entries/1
DELETE http://localhost:8080/entries/1
```

## Beispiel POST Request für Entry

```json
{
  "checkIn": "2025-12-09T08:00:00",
  "checkOut": "2025-12-09T17:00:00",
  "employeeId": 1,
  "categoryId": 1,
  "tagIds": [1, 2, 3]
}
```

## Diskussionspunkte

### Warum Java statt SQL?
✅ **Vorteile von Java:**
- Typsicherheit
- Einfache Verwaltung von Beziehungen
- Wartbar und erweiterbar
- Kann Business-Logik nutzen
- IDE-Unterstützung

❌ **Nachteile von SQL:**
- Manuelle Verwaltung von Foreign Keys
- Keine Typsicherheit
- Schwieriger zu warten

### Warum beim Start laden?
✅ **Vorteile:**
- Sofort einsatzbereit
- Konsistente Entwicklungsumgebung
- Spart Zeit

⚠️ **Hinweis:** In Produktion sollte `app.testdata.load-on-startup=false` gesetzt werden!

### Warum zusätzlich ein Endpunkt?
✅ **Flexibilität:**
- Daten neu laden ohne Neustart
- Testen verschiedener Szenarien
- Daten zurücksetzen während Tests
