package digit.repository.Utility;

import java.util.Date;

public class DateUtils {
    public static Date convertBigintToDate(long timestamp) {
        return new Date(timestamp); // If the timestamp is in milliseconds
        // If it's in seconds, use the following line instead:
        // return new Date(timestamp * 1000);
    }
}
