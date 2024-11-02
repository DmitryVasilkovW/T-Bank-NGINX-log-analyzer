package backend.academy.log.analyzer;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(?<remoteAddr>\\S+) - (?<remoteUser>\\S+) \\[(?<timeLocal>[^]]+)] \"(?<request>[^\"]*)\" " +
            "(?<status>\\d{3}) (?<bodyBytesSent>\\d+) \"(?<httpReferer>[^\"]*)\" \"(?<httpUserAgent>[^\"]*)\"$"
    );
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public Optional<LogRecord> parseLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            LocalDateTime timeLocal = LocalDateTime.parse(matcher.group("timeLocal"), DATE_FORMAT);
            return Optional.of(new LogRecord(
                timeLocal,
                matcher.group("remoteAddr"),
                matcher.group("remoteUser"),
                Integer.parseInt(matcher.group("status")),
                Long.parseLong(matcher.group("bodyBytesSent")),
                matcher.group("httpReferer"),
                matcher.group("httpUserAgent")
            ));

        }
        return Optional.empty();
    }
}

