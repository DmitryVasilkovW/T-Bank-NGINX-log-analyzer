package backend.academy.log.analyzer.model;

import java.time.OffsetDateTime;

public record LogRecord(
    OffsetDateTime timeLocal,
    String remoteAddr,
    String resource,
    int statusCode,
    long responseSize,
    String httpReferer,
    String httpUserAgent
) {
}
