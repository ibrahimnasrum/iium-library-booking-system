# Changelog

All notable changes to this project are documented in this file.

## [Unreleased] - 2026-01-21
### Added
- New professional **Login screen** as the application entry point (replaced auto-login).
- Project UML diagrams (source: `uml/full_project_diagram.puml`, rendered `uml/full_project_diagram.png`).
- `tools/render-uml.bat` helper for local rendering with `plantuml.jar`.

### Changed
- Facility statuses: preserve **Closed** and **Maintenance** statuses (no longer forced to AVAILABLE on startup).
- Facility status refresh and filter logic improved; debug logging added to `FacilitiesPage`.
- Main application startup flow and stylesheet loading made robust (null-safe).

### Fixed
- Fixed login and quick-login button compilation issues.
- Fixed a duplicate main method and other compilation anomalies in `MainApplication.java`.
- Fixed filter edge cases so Closed facilities appear when filtering by status.

### Documentation
- Updated `README.md`, `QUICK_START.md`, `USER_MANUAL.md`, and `DEVELOPMENT_GUIDE.md` to reflect changes.
- Added UML files and rendering guide (`uml/` and `tools/render-uml.bat`).

## [2025-12-XX] - Previous updates
- Initial project scaffolding and features (booking system core, UI pages, services).


*For details on each change, check individual commit messages in Git history.*
