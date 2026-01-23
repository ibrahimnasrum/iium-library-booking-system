import java.time.LocalDateTime;

public class TimeTest {
    public static void main(String[] args) {
        // Test minimum advance time validation
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current time: " + now);

        // Test 1: 15 minutes from now (should fail)
        LocalDateTime tooSoon = now.plusMinutes(15);
        boolean result1 = hasMinimumAdvanceTime(tooSoon);
        System.out.println("15 minutes ahead: " + tooSoon + " -> " + result1 + " (should be false)");

        // Test 2: 30 minutes from now (should pass)
        LocalDateTime justRight = now.plusMinutes(30);
        boolean result2 = hasMinimumAdvanceTime(justRight);
        System.out.println("30 minutes ahead: " + justRight + " -> " + result2 + " (should be true)");

        // Test 3: 45 minutes from now (should pass)
        LocalDateTime plenty = now.plusMinutes(45);
        boolean result3 = hasMinimumAdvanceTime(plenty);
        System.out.println("45 minutes ahead: " + plenty + " -> " + result3 + " (should be true)");
    }

    public static boolean hasMinimumAdvanceTime(LocalDateTime startTime) {
        final int MIN_ADVANCE_MINUTES = 30;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minimumStartTime = now.plusMinutes(MIN_ADVANCE_MINUTES);

        System.out.println("  Now: " + now);
        System.out.println("  Min start time: " + minimumStartTime);
        System.out.println("  Requested start: " + startTime);
        System.out.println("  Is after or equal: " + (startTime.isAfter(minimumStartTime) || startTime.isEqual(minimumStartTime)));

        return startTime.isAfter(minimumStartTime) || startTime.isEqual(minimumStartTime);
    }
}