# Statistics Feature - Zeiterfassung pro Tag

## Übersicht

Das Statistics-Feature ermöglicht es, die erfasste Arbeitszeit pro Tag zusammenzufassen und abzurufen.

## Implementierung

### 1. TimeSummaryDTO
**Datei:** [TimeSummaryDTO.java](src/main/java/ch/zli/m223/dto/TimeSummaryDTO.java)

Data Transfer Object für die Übertragung von Zeitstatistiken.

**Felder:**
- `date` (LocalDate) - Das Datum
- `duration` (Duration) - Die Gesamtdauer für diesen Tag

**Methoden:**
- `getFormattedDuration()` - Formatiert die Duration als `HH:mm:ss` String

### 2. TimeSummaryService
**Datei:** [TimeSummaryService.java](src/main/java/ch/zli/m223/service/TimeSummaryService.java)

Service zur Berechnung der Zeitstatistiken.

**Methode:** `calculateSummaryPerDay(List<Entry> entries)`
- **Parameter:** Liste von Entry-Objekten
- **Rückgabe:** Liste von TimeSummaryDTO-Objekten
- **Funktionsweise:**
  1. Filtert unvollständige Einträge (ohne checkOut) heraus
  2. Gruppiert Einträge nach Datum (checkIn-Datum)
  3. Berechnet die Gesamtdauer pro Tag
  4. Sortiert nach Datum (neueste zuerst)

### 3. EntryService Erweiterung
**Datei:** [EntryService.java](src/main/java/ch/zli/m223/service/EntryService.java)

**Neue Methode:** `getTimeSummaries()`
- Lädt alle Entries aus der Datenbank
- Übergibt sie an den TimeSummaryService
- Gibt die berechneten Statistiken zurück

### 4. StatisticsController
**Datei:** [StatisticsController.java](src/main/java/ch/zli/m223/controller/StatisticsController.java)

REST Controller für Statistik-Endpunkte.

**Endpunkt:** `GET /statistics/time-summaries`
- Ruft `entryService.getTimeSummaries()` auf
- Konvertiert die DTOs in eine Map
- Format: `{ "YYYY-MM-DD": "HH:mm:ss" }`

## API Endpunkt

### GET /statistics/time-summaries

**Request:**
```bash
curl http://localhost:8080/statistics/time-summaries
```

**Response:**
```json
{
  "2025-12-09": "09:30:00",
  "2025-12-08": "08:15:00",
  "2025-12-07": "07:45:00",
  "2025-12-06": "08:00:00",
  "2025-12-05": "09:15:00",
  "2025-12-04": "09:00:00",
  "2025-12-03": "04:00:00"
}
```

## Beispiel-Berechnung

**Entries für 2025-12-09:**
- Entry 1: 08:00 - 12:30 = 4,5 Stunden
- Entry 2: 13:00 - 18:00 = 5 Stunden
- **Total: 09:30:00**

**Entries für 2025-12-08:**
- Entry 1: 09:00 - 17:15 = 8,25 Stunden
- **Total: 08:15:00**

## Architektur

```
┌─────────────────────────────────────────────┐
│     StatisticsController                    │
│  GET /statistics/time-summaries             │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│          EntryService                       │
│  getTimeSummaries()                         │
│  - Lädt alle Entries                        │
│  - Ruft TimeSummaryService auf              │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│      TimeSummaryService                     │
│  calculateSummaryPerDay(entries)            │
│  - Filtert incomplete Entries               │
│  - Gruppiert nach Datum                     │
│  - Berechnet Summen                         │
│  - Sortiert Ergebnisse                      │
└─────────────────────────────────────────────┘
```

## Besonderheiten

### 1. Nur abgeschlossene Einträge
Laufende Einträge (ohne `checkOut`) werden **nicht** in die Statistik einbezogen.

```java
.filter(entry -> entry.getCheckOut() != null)
```

### 2. Gruppierung nach CheckIn-Datum
Die Gruppierung erfolgt basierend auf dem `checkIn`-Datum, nicht auf dem `checkOut`-Datum.

```java
.groupingBy(entry -> entry.getCheckIn().toLocalDate())
```

### 3. Sortierung
Ergebnisse werden nach Datum sortiert (neueste zuerst).

```java
summaries.sort((a, b) -> b.getDate().compareTo(a.getDate()))
```

### 4. Duration-Berechnung
Die Dauer wird mit Java's `Duration.between()` berechnet:

```java
Duration.between(entry.getCheckIn(), entry.getCheckOut())
```

## Testing mit Testdaten

Nach dem Laden der Testdaten sollten Sie ca. 8-9 verschiedene Tage sehen:

```bash
# Testdaten laden
curl -X POST http://localhost:8080/api/testdata

# Statistiken abrufen
curl http://localhost:8080/statistics/time-summaries
```

## Erweiterungsmöglichkeiten

### 1. Filterung nach Employee
```java
GET /statistics/time-summaries?employeeId=1
```

### 2. Filterung nach Datumsbereich
```java
GET /statistics/time-summaries?from=2024-08-01&to=2024-08-31
```

### 3. Zusätzliche Statistiken
- Durchschnittliche Arbeitszeit pro Tag
- Überstunden-Berechnung
- Arbeitszeit pro Kategorie
- Arbeitszeit pro Tag der Woche

### 4. Detaillierte Statistiken
```java
GET /statistics/detailed-summaries
```
Könnte zusätzlich zurückgeben:
- Anzahl der Einträge pro Tag
- Frühester/spätester Check-in
- Längste/kürzeste Arbeitszeit

## Dependency Injection

Der `TimeSummaryService` ist als `@ApplicationScoped` registriert und wird automatisch vom CDI-Container verwaltet:

```java
@ApplicationScoped
public class TimeSummaryService { ... }
```

Er wird im `EntryService` injiziert:

```java
@Inject
TimeSummaryService timeSummaryService;
```

## Fehlerbehandlung

Aktuell gibt es keine spezielle Fehlerbehandlung. Mögliche Erweiterungen:

1. **Leere Liste:** Wenn keine Entries vorhanden sind, wird eine leere Map zurückgegeben
2. **Ungültige Daten:** Entries mit `checkOut` vor `checkIn` führen zu negativen Durationen

Empfohlene Verbesserungen:
```java
if (entry.getCheckOut().isBefore(entry.getCheckIn())) {
    // Log warning or throw exception
}
```

## Performance-Überlegungen

Bei großen Datenmengen könnte die Performance verbessert werden durch:

1. **Datenbankabfrage optimieren:** Nur benötigte Felder laden
2. **Pagination:** Statistiken in Chunks laden
3. **Caching:** Häufig abgerufene Statistiken cachen
4. **Aggregation in Datenbank:** SQL-Aggregation statt Java-Streams

```sql
SELECT DATE(check_in), SUM(EXTRACT(EPOCH FROM (check_out - check_in)))
FROM entry
WHERE check_out IS NOT NULL
GROUP BY DATE(check_in)
ORDER BY DATE(check_in) DESC
```
