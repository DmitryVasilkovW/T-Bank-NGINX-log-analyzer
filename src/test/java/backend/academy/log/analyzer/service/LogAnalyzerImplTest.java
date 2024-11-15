package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogAnalyzerImplTest {
    @Mock
    private LogParserImpl logParserMock;

    @InjectMocks
    private LogAnalyzerImpl logAnalyzer;

    @Test
    void testSetFromAndSetTo() {
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = OffsetDateTime.now();
        logAnalyzer.setFrom(from);
        logAnalyzer.setTo(to);

        assertEquals(from, logAnalyzer.from());
        assertEquals(to, logAnalyzer.to());
    }

    @Test
    void testAddIfInRangeIndirectlyThroughReadLogsByURL() throws Exception {
        OffsetDateTime time = OffsetDateTime.parse("2015-05-17T08:05:32+00:00");

        LogRecord record = new LogRecord(time, "93.180.71.3", "/downloads/product_1", 304, 0L, "-",
            "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)");

        when(logParserMock.parseLine(anyString())).thenReturn(Optional.of(record));

        logAnalyzer.readLogs("https://example.com");

        assertFalse(logAnalyzer.logRecords().isEmpty());
        assertEquals(record, logAnalyzer.logRecords().getFirst());
    }

    @Test
    void testAddIfInRangeIndirectlyThroughReadLogsByFile() throws Exception {
        OffsetDateTime time = OffsetDateTime.parse("2015-05-17T08:05:32+00:00");

        LogRecord record = new LogRecord(time, "93.180.71.3", "/downloads/product_1", 304, 0L, "-",
            "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)");

        when(logParserMock.parseLine(anyString())).thenReturn(Optional.of(record));

        logAnalyzer.readLogs("src/test/resources/logs.log");

        assertFalse(logAnalyzer.logRecords().isEmpty());
        assertEquals(record, logAnalyzer.logRecords().getFirst());
    }

    @Test
    void testParseLine() {
        String logLine =
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        OffsetDateTime expectedTime = OffsetDateTime.parse("2015-05-17T08:05:32+00:00");
        String expectedRemoteAddr = "93.180.71.3";
        String expectedResource = "/downloads/product_1";
        int expectedStatusCode = 304;
        long expectedResponseSize = 0L;
        String expectedHttpReferer = "-";
        String expectedHttpUserAgent = "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)";

        LogParserImpl logParser = new LogParserImpl();

        Optional<LogRecord> result = logParser.parseLine(logLine);

        assertTrue(result.isPresent(), "Лог-запись должна быть успешно распознана");
        LogRecord logRecord = result.get();

        assertEquals(expectedTime, logRecord.getTimeLocal());
        assertEquals(expectedRemoteAddr, logRecord.getRemoteAddr());
        assertEquals(expectedResource, logRecord.getResource());
        assertEquals(expectedStatusCode, logRecord.getStatusCode());
        assertEquals(expectedResponseSize, logRecord.getResponseSize());
        assertEquals(expectedHttpReferer, logRecord.getHttpReferer());
        assertEquals(expectedHttpUserAgent, logRecord.getHttpUserAgent());
    }
}
