package backend.academy.log.analyzer;

import java.time.LocalDateTime;

public class LogRecord {
    private final LocalDateTime timeLocal;
    private final String remoteAddr;
    private final String resource;
    private final int statusCode;
    private final long responseSize;
    private final String httpReferer;
    private final String httpUserAgent;

    public LogRecord(LocalDateTime timeLocal, String remoteAddr, String resource, int statusCode,
        long responseSize, String httpReferer, String httpUserAgent) {
        this.timeLocal = timeLocal;
        this.remoteAddr = remoteAddr;
        this.resource = resource;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
        this.httpReferer = httpReferer;
        this.httpUserAgent = httpUserAgent;
    }

    public LocalDateTime getTimeLocal() {
        return timeLocal;
    }

    public String getResource() {
        return resource;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public String getHttpReferer() {
        return httpReferer;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }
}
