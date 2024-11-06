package backend.academy.log.analyzer.model;

import java.time.OffsetDateTime;

public class LogRecord {
    private final OffsetDateTime timeLocal;
    private final String remoteAddr;
    private final String resource;
    private final int statusCode;
    private final long responseSize;
    private final String httpReferer;
    private final String httpUserAgent;

    public LogRecord(OffsetDateTime timeLocal, String remoteAddr, String resource, int statusCode,
        long responseSize, String httpReferer, String httpUserAgent) {
        this.timeLocal = timeLocal;
        this.remoteAddr = remoteAddr;
        this.resource = resource;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
        this.httpReferer = httpReferer;
        this.httpUserAgent = httpUserAgent;
    }

    public OffsetDateTime getTimeLocal() {
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

    public String getRemoteAddr() {
        return remoteAddr;
    }
}
