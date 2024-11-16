package backend.academy.log.analyzer.service.parser.impl;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.service.parser.LogParser;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParserImpl implements LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(?<remoteAddr>\\S+) - (?<remoteUser>\\S+) \\[(?<timeLocal>[^]]+)] \"(?<method>\\S+) (?<resource>\\S+) (?<protocol>[^\"]*)\" " +
            "(?<status>\\d{3}) (?<bodyBytesSent>\\d+) \"(?<httpReferer>[^\"]*)\" \"(?<httpUserAgent>[^\"]*)\"$"
    );
    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    @Override
    public Optional<LogRecord> parseLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            OffsetDateTime timeLocal = OffsetDateTime.parse(matcher.group("timeLocal"), DATE_FORMAT);
            return Optional.of(new LogRecord(
                timeLocal,
                matcher.group("remoteAddr"),
                matcher.group("resource"),
                Integer.parseInt(matcher.group("status")),
                Long.parseLong(matcher.group("bodyBytesSent")),
                matcher.group("httpReferer"),
                matcher.group("httpUserAgent"),
                matcher.group("method")
            ));
        }
        return Optional.empty();
    }
}


