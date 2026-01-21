Title: docs: update docs & add UML diagrams; document login flow and recent fixes

Description:
- Added UML diagrams (source: `uml/full_project_diagram.puml`, rendered: `uml/full_project_diagram.png`) and a helper script `tools/render-uml.bat` to make local rendering easier.
- Replaced auto-login with a proper **Login screen** and documented the change across `README.md`, `QUICK_START.md`, `USER_MANUAL.md`, and `API_DOCUMENTATION.md`.
- Preserved Closed & Maintenance facility statuses and fixed facility filter/display bugs; added debug logging to `FacilitiesPage` to aid troubleshooting.
- Fixed compilation issues and minor bugs in `MainApplication` and `LoginPage` (duplicate main, missing imports, quick-login button scope), and improved stylesheet handling.
- Added `CHANGELOG.md` summarizing recent updates and next steps for testing.

Testing performed:
- Manual verification of Login screen appearance and login flow
- Confirmed closed facilities show up and filtering works as expected
- Confirmed application runs and does not crash when stylesheet is absent

Notes:
- The changes are on branch `docs/update-uml`. Please review and merge when ready.
