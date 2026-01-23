import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeFilterTest {
    public static void main(String[] args) {
        System.out.println("Testing time filtering logic...");

        // Test for today
        LocalDate today = LocalDate.now();
        System.out.println("\nFor today (" + today + "):");

        LocalDateTime earliestAllowed;
        if (today.equals(LocalDate.now())) {
            // Today: only times at least 30 minutes from now
            earliestAllowed = LocalDateTime.now().plusMinutes(30);
            System.out.println("Earliest allowed time: " + earliestAllowed);
        } else {
            // Future date: any business hour
            earliestAllowed = LocalDateTime.of(today, LocalTime.of(8, 0));
            System.out.println("Earliest allowed time: " + earliestAllowed);
        }

        List<String> availableTimes = new ArrayList<>();
        for (int hour = 8; hour <= 22; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                LocalDateTime timeSlot = LocalDateTime.of(today, LocalTime.of(hour, minute));
                if (timeSlot.isAfter(earliestAllowed) || timeSlot.isEqual(earliestAllowed) ||
                    !today.equals(LocalDate.now())) {
                    String time = String.format("%02d:%02d", hour, minute);
                    availableTimes.add(time);
                }
            }
        }

        System.out.println("Available times: " + availableTimes);

        // Test for tomorrow
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        System.out.println("\nFor tomorrow (" + tomorrow + "):");

        if (tomorrow.equals(LocalDate.now())) {
            earliestAllowed = LocalDateTime.now().plusMinutes(30);
        } else {
            earliestAllowed = LocalDateTime.of(tomorrow, LocalTime.of(8, 0));
        }
        System.out.println("Earliest allowed time: " + earliestAllowed);

        availableTimes.clear();
        for (int hour = 8; hour <= 22; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                LocalDateTime timeSlot = LocalDateTime.of(tomorrow, LocalTime.of(hour, minute));
                if (timeSlot.isAfter(earliestAllowed) || timeSlot.isEqual(earliestAllowed) ||
                    !tomorrow.equals(LocalDate.now())) {
                    String time = String.format("%02d:%02d", hour, minute);
                    availableTimes.add(time);
                }
            }
        }

        System.out.println("Available times: " + availableTimes);
    }
}