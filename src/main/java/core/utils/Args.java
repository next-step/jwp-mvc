package core.utils;

import java.util.regex.Matcher;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class Args {

    private Args() {
    }

    public static void check(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static Matcher checkPattern(final Matcher matcher, final String message) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException(message);
        }
        return matcher;
    }

    public static <T> T notNull(final T argument, final String message) {
        if (argument == null) {
            throw new IllegalArgumentException(message);
        }
        return argument;
    }
}
