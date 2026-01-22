# IIUM Library Booking System — Project Report

> Note: Replace placeholder names (e.g., **Person-in-charge: <Name>**) with the actual team member names before final submission. Add GUI screenshots to `docs/images/` and update references accordingly.

---

## 1. Introduction

### Project Background
The IIUM Library Booking System is designed to modernize and centralize the booking of library facilities such as carrel rooms, discussion rooms, viewing rooms, and computer labs. The project addresses the need for an accessible, role-based, and conflict-free booking system tailored to the IIUM community (students, postgraduate students, staff, and admins). The previous ad-hoc/manual booking process led to double-bookings, inconsistent privilege enforcement, and limited transparency. This system provides a robust JavaFX desktop application with IIUM matric-based authentication, real-time facility status updates, role-based access control, and conflict detection.

**Why a new system is needed:**
- Eliminate unauthorized bookings and conflicts.
- Provide clear role-based privileges aligned with IIUM matriculation conventions.
- Offer a modern, user-friendly GUI for students, staff, and administrators.
- Provide auditability and easier administrative control.

---

## 2. Problem Statement
The existing approach to facility bookings at IIUM suffers from the following problems:
- Manual or semi-manual bookings cause collisions and inconsistent state across systems.
- No robust role-based enforcement (e.g., postgraduate-only rooms were sometimes booked by regular students).
- No single user-facing application to browse, filter, and book facilities with immediate confirmation and status updates.
- Lack of logging and debugging tools made it difficult for administrators to trace issues.

This project addresses those gaps by providing a single desktop application with clearly enforced policies, conflict detection, and administrative controls.

---

## 3. Project Objectives
- Implement a secure login system using IIUM matric number patterns and role assignment.
- Provide a responsive JavaFX GUI for browsing, filtering, and booking facilities.
- Enforce booking policies and prevent booking conflicts automatically.
- Present up-to-date facility status (Available, Booked, Maintenance, Closed) and allow manual admin overrides.
- Provide maintainable code with clear separation of concerns (Model, Service, View) and detailed documentation (UML & architecture docs).
- Ensure the system is testable and extensible for future improvements (API, networked multi-user mode).

---

## 4. Requirements Analysis
### Functional Requirements
- User authentication by IIUM matric number (roles derived from matric prefix; mapping is configurable).
- Facility browsing with search, location, and status filters.
- Timing-based booking with validation and conflict checking.
- Booking history and cancellation features for users.
- Admin panel to update facility status (Maintenance/Closed/Available) and manage bookings.
- Real-time status updates and manual refresh option.

### Non-functional Requirements
- Desktop app using Java 21 and JavaFX 21.0.9.
- Local-first data with easily extensible service layer for future back-end integration.
- Clear, accessible GUI with role-appropriate UI elements.
- Maintainable code with unit-testable services.

---

## 5. Design Phase
### Object Identification & Class Overview
During analysis we focused on the domain entities and the behaviors required by the system. The main classes identified and their responsibilities are below. The persons-in-charge are placeholders — replace them with actual team members.

- Facility (abstract)
  - Responsibility: represent rooms/areas with status and reservation rules
  - Person-in-charge: <Name>
- Room (extends Facility)
  - Responsibility: legacy constructors and facility-type mapping
  - Person-in-charge: <Name>
- Booking
  - Responsibility: model reservation (start, end, status)
  - Person-in-charge: <Name>
- User
  - Responsibility: represent a user with matric, role and bookings
  - Person-in-charge: <Name>
- AuthService
  - Responsibility: login and role determination
  - Person-in-charge: <Name>
- FacilityService
  - Responsibility: facility data management
  - Person-in-charge: <Name>
- BookingService
  - Responsibility: booking creation, conflict detection and cleanup
  - Person-in-charge: <Name>
- BookingPolicy
  - Responsibility: business rule validation (privileges, hours)
  - Person-in-charge: <Name>
- FacilityCard (UI)
  - Responsibility: display facility and status
  - Person-in-charge: <Name>
- FacilitiesPage (UI)
  - Responsibility: list/filter facilities and refresh statuses
  - Person-in-charge: <Name>
- LoginPage, MainApplication, MainLayout
  - Responsibility: app entry, login flow and navigation
  - Person-in-charge: <Name>

### UML Class Diagram
The detailed UML class diagram is included in the project: `uml/full_project_diagram.png`.

(You can open `uml/full_project_diagram.png` or render `uml/full_project_diagram.puml` using the `tools/render-uml.bat` script.)

### GUI mockup sketches
Place GUI screenshots or sketches into `docs/images/` and reference them here. Example placeholder:
- `docs/images/login-screen.png` — Login screen mockup showing matric field and quick-login buttons.
- `docs/images/facilities-page.png` — Facilities search and card view.

### OOP Concepts Implemented
- Encapsulation: classes hide internal state via private fields and expose behavior via methods.
- Inheritance: `Room` extends `Facility` (IS-A relationship).
- Polymorphism: UI components and services accept `Facility` references; different facility types can behave uniformly.
- Abstraction: `Facility` / `Booking` model domain concepts.
- Composition: `MainLayout` contains pages and `FacilitiesPage` creates `FacilityCard` components.

---

## 6. Development Phase
### Screenshots & Descriptions
(Replace placeholder filenames with actual screenshots captured from the running app.)
- `docs/images/login-screen.png` — Login screen: enter IIUM matric number and optional password; quick-login buttons for admin/student/postgrad for testing purposes.
- `docs/images/facilities-page.png` — Facilities page: search bar, location and status filters, facility cards showing status badges and click-to-detail behavior.
- `docs/images/booking-detail.png` — Facility detail and booking controls: date/time selection, booking confirmation dialog.

### Source code & responsibilities
Actual source code is in `src/`. For submission, include all `.java` files in a ZIP and provide names and responsibilities in Appendix 1 (see below).

**How to submit source code**
- Create a zip: `zip -r iium-library-booking-system-src.zip src/` or use your preferred archiver.
- Upload the zip to iTa’leem or provide the GitHub link to the repository (preferred): https://github.com/ibrahimnasrum/iium-library-booking-system

---

## 7. Lessons Learned
- Benefits:
  - Learned to design with clear separation between UI and business logic.
  - Implemented robust validation and conflict detection logic that anticipates real-world booking scenarios.
  - Gained experience with JavaFX, Java 21, and UML-driven design.

- Challenges:
  - Reconciling legacy data models (`Room` constructors) with new typed enums and services required careful mapping.
  - Ensuring the UI reflected real-time updates without a backend required careful state management in services and pages.

---

## 8. Conclusions & Future Work
### Conclusions
The IIUM Library Booking System provides a solid, maintainable foundation for booking management with role-based access, conflict detection, and a friendly UI. The project meets the main objectives and improves reliability and manageability over the previous ad-hoc approach.

### Limitations & Recommendations
- Current implementation is single-user/local-first. Recommend future work to support multi-user sync (server backend) and persistent database storage.
- Add automated UI tests and E2E workflows for critical booking flows.
- Add notification service for email/SMS confirmations (future feature).

---

## References
- Java 21 (JDK). https://adoptium.net/
- JavaFX 21.0.9. https://openjfx.io/
- PlantUML. https://plantuml.com/

---

## Appendix 1 — Source Code Index & Person-in-Charge
> Include the `.java` files here and assign person-in-charge names. For group submissions, list each class with the associated author.

- `src/model/User.java` — Person-in-charge: <Name>
- `src/model/Facility.java` — Person-in-charge: <Name>
- `src/model/Booking.java` — Person-in-charge: <Name>
- `src/model/Room.java` — Person-in-charge: <Name>
- `src/model/Equipment.java` — Person-in-charge: <Name>
- `src/model/services/AuthService.java` — Person-in-charge: <Name>
- `src/model/services/FacilityService.java` — Person-in-charge: <Name>
- `src/model/services/BookingService.java` — Person-in-charge: <Name>
- `src/model/services/BookingPolicy.java` — Person-in-charge: <Name>
- `src/view/MainApplication.java` — Person-in-charge: <Name>
- `src/view/MainLayout.java` — Person-in-charge: <Name>
- `src/view/pages/LoginPage.java` — Person-in-charge: <Name>
- `src/view/pages/FacilitiesPage.java` — Person-in-charge: <Name>
- `src/view/components/FacilityCard.java` — Person-in-charge: <Name>


---

If you'd like, I can:
- Embed rendered screenshots into the doc (if you provide the images),
- Expand Appendix 1 with full copy-paste of each `.java` file (or attach them into a ZIP),
- Produce separate per-package UML diagrams for better readability.

Tell me what to do next (embed screenshots, append source files, or generate per-package diagrams).