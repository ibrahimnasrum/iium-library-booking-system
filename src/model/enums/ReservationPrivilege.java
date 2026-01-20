package model.enums;

public enum ReservationPrivilege {
    OPEN,                    // Available to all users
    STUDENT_ONLY,           // Students only
    STAFF_ONLY,             // Academic staff only
    POSTGRADUATE_ONLY,      // Postgraduate students only
    SPECIAL_NEEDS_ONLY,     // Users with special needs
    BOOK_VENDORS_ONLY,      // For book vendors
    LIBRARY_USE_ONLY        // Reserved for library activities
}