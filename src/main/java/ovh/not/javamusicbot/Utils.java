package ovh.not.javamusicbot;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.time.DurationFormatUtils;

public abstract class Utils {
    public static final String HASTEBIN_URL = "https://hastebin.com/documents";
    private static final String DURATION_FORMAT = "mm:ss";
    private static final String DURATION_FORMAT_LONG = "HH:mm:ss";

    public static String formatDuration(long duration) {
        return DurationFormatUtils.formatDuration(duration, DURATION_FORMAT);
    }

    public static String formatLongDuration(long duration) {
        return DurationFormatUtils.formatDuration(duration, DURATION_FORMAT_LONG);
    }

    static PrivateChannel getPrivateChannel(User user) {
        try {
            return user.getPrivateChannel();
        } catch (IllegalStateException ignored) {
            return user.openPrivateChannel().complete();
        }
    }
}
