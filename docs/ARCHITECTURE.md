# IIUM Library Booking System — Architecture Documentation

## Overview
This document explains the core architecture of the IIUM Library Booking System, focusing on the domain (model), service layer, and view layer. It describes key classes, their relationships (associations, composition, inheritance, dependencies), typical interaction flows, and design rationale.

Files & Diagrams
- UML source: `uml/full_project_diagram.puml`
- Rendered UML: `uml/full_project_diagram.png`
- Architectural guide: `docs/ARCHITECTURE.md` (this file)

---

## Layers & Responsibilities
- **Model (Domain)**: `src/model/` — `Facility`, `Booking`, `User`, `Equipment`, `Room`, etc. Represents domain entities and enums (`Role`, `FacilityStatus`, etc.).
- **Service Layer**: `src/model/services/` — `AuthService`, `FacilityService`, `BookingService`, `BookingPolicy`. Encapsulates business logic and validation.
- **View (Presentation)**: `src/view/`, `src/view/pages/`, `src/view/components/` — JavaFX UI, pages and components (e.g., `LoginPage`, `FacilitiesPage`, `FacilityCard`).

---

## Key Classes (catalog)
Each entry shows purpose, key attributes, and primary methods.

### Facility (abstract)
- Purpose: Domain representation for bookable resources (rooms, study areas).
- Key attributes: `id`, `name`, `type: FacilityType`, `status: FacilityStatus`, `privilege: ReservationPrivilege`, `capacity`.
- Methods: `isAvailable()`, `setStatus(FacilityStatus)`.
- Concrete subclass: `Room` (legacy / specific constructor mapping).

### Booking
- Purpose: Represents a reservation for a facility.
- Key attributes: `id`, `facilityId`, `userId`, `start`, `end`, `status: BookingStatus`.
- Methods: `overlapsWith(Booking)` for conflict detection.

### User
- Purpose: System user with role-based privileges.
- Key attributes: `matricNo`, `role: Role`, `myBookings`.
- Methods: `canBook(Facility)`, `addBooking(Booking)`.

### AuthService
- Purpose: Authentication and role determination from matric number.
- Key methods: `login(matric, pwd): User`, `isAdmin(User)`.

### FacilityService
- Purpose: Manage and query facilities.
- Key methods: `getAllFacilities()`, `findFacilityById()`, `getFacilitiesByStatus()`.

### BookingPolicy
- Purpose: Centralized business rules (privilege checks, time rules).
- Key methods: `validateBooking(User, Facility, start, end): boolean`.

### BookingService
- Purpose: Create/cancel bookings, detect conflicts, maintain booking list.
- Key methods: `createBooking(...)`, `cancelBooking(id)`, `getUserBookings(userId)`, `hasBookingConflict(...)`.

### FacilityCard (UI)
- Purpose: Visual representation of a facility in the UI.
- Key methods: `updateStatus(FacilityStatus)`, `setOnClick(Runnable)`.

### FacilitiesPage (UI page)
- Purpose: List, search, filter facilities.
- Key methods: `loadData()`, `filterFacilities()`, `refreshFacilityStatuses()`.

### MainApplication & MainLayout
- Entry point and application layout. Manages login flow (`LoginPage`) and shows `MainLayout` (sidebar navigation and content pages).

---

## Relationship Types & Notes
- **Inheritance (IS-A)**: `Room --|> Facility` (reuses properties/behavior).
- **Composition/Containment**: `MainLayout *-- FacilitiesPage`, `FacilitiesPage *-- FacilityCard`. The layout/pages own their children in the UI hierarchy.
- **Association (has/owns)**: `User "1" -- "0..*" Booking` (users own bookings); `Facility "1" -- "0..*" Booking` (facility can have multiple bookings over time).
- **Dependency/Usage (->)**: UI pages depend on services (e.g., `LoginPage --> AuthService`, `FacilitiesPage --> FacilityService`). These indicate runtime calls rather than ownership.
- **Service dependencies**: `BookingService` uses `BookingPolicy` and `FacilityService` to perform validation and checks.

---

## Typical Interaction Flows (sequences)
### Login
1. `LoginPage` calls `AuthService.login(matric, pwd)`
2. On success, `MainApplication.handleLoginSuccess(User)` initializes data and shows `MainLayout`.

### Booking a Facility
1. User selects a facility in `FacilitiesPage` (via `FacilityCard`).
2. `BookingService.createBooking(User, facilityId, start, end)` is invoked.
3. `BookingService` calls `BookingPolicy.validateBooking(...)` and checks `hasBookingConflict(...)`.
4. If valid, booking saved and `Facility` status updated => UI refresh via `FacilityCard.updateStatus()`.

---

## Design Rationale & Benefits
- **Separation of concerns**: Views do not contain business rules — services do. This improves testability and maintainability.
- **Single source of truth**: `FacilityService` centralizes facility data, preventing inconsistent copies across UI pages.
- **Testable policies**: `BookingPolicy` can be unit-tested independently from UI.

---

## Implementation Notes & Files
- Auth: `src/model/services/AuthService.java`
- Booking: `src/model/services/BookingService.java`, `src/model/services/BookingPolicy.java`
- Facilities: `src/model/services/FacilityService.java`, `src/view/pages/FacilitiesPage.java`, `src/view/components/FacilityCard.java`
- UI layout: `src/view/MainLayout.java`, `src/view/MainApplication.java`

---

## Suggestions & Next Steps
- Document all public methods in `API_DOCUMENTATION.md` (some classes already included; expand further).
- Consider adding interfaces for services to make unit testing (mocking) easier (e.g., `IFacilityService`).
- If desired, split UML into smaller diagrams (Domain / Services / UI) for readability.

---

If you want, I can:
- Commit this doc and link it from the README, or
- Generate per-package diagrams (domain-only, service-only) and add them to `uml/`.

Which would you prefer next?