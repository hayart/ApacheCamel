/*
 * Copyright (c) Worldline 2022 - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 **/
package am.developer.camel.utility;

import java.time.Duration;

/**
 * Utility class for Camel processors
 *
 * @author a509159
 * @version 2.0.38
 */
public final class ProcessUtility {

    /**
     * Constructor
     */
    private ProcessUtility() {
    }

    /**
     * Converts provided minutes to milliseconds
     *
     * @param minutes that need to convert
     * @return provided minutes as milliseconds
     */
    public static long minutesToMillis(long minutes) {
        return minutes * BatchConstants.MINUTE_AS_SECONDS * BatchConstants.MINUTE_AS_MILLIS;
    }

    /**
     * Converts provided days to milliseconds
     *
     * @param periodInDay that need to convert
     * @return provided days as milliseconds
     */
    public static long daysToMillis(long periodInDay) {
        return periodInDay * BatchConstants.DAY_AS_HOURS * BatchConstants.HOUR_AS_MINUTES * BatchConstants.MINUTE_AS_SECONDS * BatchConstants.MINUTE_AS_MILLIS;
    }

    /**
     * Finds duration of something and converts it to string
     *
     * @param startTime start time of something
     * @return string representation of duration time
     */
    public static String durationTime(final long startTime) {
        return durationTimeByMillis(System.currentTimeMillis() - startTime);
    }

    /**
     * Finds duration of something and converts it to string
     *
     * @param millis duration time as millis
     * @return string representation of duration time
     */
    public static String durationTimeByMillis(final long millis) {
        Duration duration = Duration.ofMillis(millis);
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

}
