package backend.academy.log.analyzer.service.parser;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogParserImplTest {
    @Mock
    private LogParserImpl mockLogParser;

    @Test
    void testParseValidLogLine() {
        String logLine =
            "127.0.0.1 - - [12/Dec/2021:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 1043 \"-\" \"Mozilla/5.0\"";
        OffsetDateTime logTime = OffsetDateTime.parse("2021-12-12T06:25:24+00:00");
        LogRecord mockLogRecord = new LogRecord(
            logTime,
            "127.0.0.1",
            "/index.html",
            200,
            1043,
            "-",
            "Mozilla/5.0",
            "GET"
        );
        when(mockLogParser.parseLine(logLine)).thenReturn(Optional.of(mockLogRecord));

        Optional<LogRecord> result = mockLogParser.parseLine(logLine);

        assertTrue(result.isPresent());
        LogRecord record = result.get();

        assertEquals("127.0.0.1", record.remoteAddr());
        assertEquals("/index.html", record.resource());
        assertEquals(200, record.statusCode());
        assertEquals(1043, record.responseSize());
        assertEquals("Mozilla/5.0", record.httpUserAgent());
        assertEquals("GET", record.method());
        assertEquals(logTime, record.timeLocal());
        assertEquals("-", record.httpReferer());

        verify(mockLogParser).parseLine(logLine);
    }

    @Test
    void testParseInvalidLogLine() {
        String logLine = "invalid log line";
        when(mockLogParser.parseLine(logLine)).thenReturn(Optional.empty());

        Optional<LogRecord> result = mockLogParser.parseLine(logLine);

        assertFalse(result.isPresent());

        verify(mockLogParser).parseLine(logLine);
    }
}
