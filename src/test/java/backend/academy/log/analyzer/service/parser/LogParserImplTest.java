package backend.academy.log.analyzer.service.parser;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LogParserImplTest {
    private final LogParserImpl logParser = new LogParserImpl();

    @Test
    void testParseValidLogLine() {
        String logLine = "127.0.0.1 - - [12/Dec/2021:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 1043 \"-\" \"Mozilla/5.0\"";
        Optional<LogRecord> result = logParser.parseLine(logLine);

        assertTrue(result.isPresent());
        LogRecord record = result.get();

        assertEquals("127.0.0.1", record.getRemoteAddr());
        assertEquals("/index.html", record.getResource());
        assertEquals(200, record.getStatusCode());
        assertEquals(1043, record.getResponseSize());
        assertEquals("Mozilla/5.0", record.getHttpUserAgent());
    }

    @Test
    void testParseInvalidLogLine() {
        String logLine = "invalid log line";
        Optional<LogRecord> result = logParser.parseLine(logLine);

        assertFalse(result.isPresent());
    }
}

