package com.trivia.core.utility;

import com.trivia.persistence.dto.server.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public final class LogUtil extends FileUtil {
    public final static Path LOG_DIR = Paths.get(SERVER_DIR + "/log");
    private final static Integer LOGS_PAGE_MAX = 100;

    private LogUtil() {}

    /**
     * Since a log row can have a fixed byte size, this could better be done MUCH faster by offsetting the search by
     * this number of bytes,
     * BUT, this would result in an inability to change the log format without changing this method
     * AND it would inhibit the flexibility of logging by demanding a certain maximum size
     * AND it could potentially create a much bigger .log file.
     * AND the Java SE 8 API is just so much cleaner (and already fast enough). :)
     */
    public static List<Log> readFromTo(int start, int size, LogType logType) throws IOException {
        start = start > LOGS_PAGE_MAX ? LOGS_PAGE_MAX : start;
        List<Log> logsObj = new ArrayList<>();

        try (Stream<String> logsRaw = Files.lines(getLogTypePath(logType), CHARSET_DEFAULT)) {
            logsRaw.skip(start).limit(size).forEach(logRaw -> logsObj.add(new Log(logRaw)));
        }
        return logsObj;
    }


    /**
     * Again, this could be done much faster by offsetting the search by the number of bytes per line. See 'readFromTo()'.
     * An additional issue regarding lazy loading is that we are reading the file two times (once by reading the
     * contents, and once by getting the total number of lines. Unfortunately, because of the nature of the lazy loading
     * provider used (and since Java methods can't return tuples), the only straightforward OOP solution to remedy this
     * is to create a larger data structure that would use composition to wrap around List<Log> and the number of lines,
     * That seems an inelegant solution and could result in  clutter. As it stands, the current system is fast enough,
     * but will warrant a reconstruction if the scale gets large enough.
     */
    public static Long getLineCount(LogType logType) throws IOException {
        Long lineCount;

        try (Stream<String> logsRaw = Files.lines(getLogTypePath(logType), CHARSET_DEFAULT)) {
            lineCount = logsRaw.count();
        }

        return lineCount;
    }

    private static Path getLogTypePath(LogType logType) {
        return Paths.get(LOG_DIR + "/" + logType.getFileName());
    }
}
