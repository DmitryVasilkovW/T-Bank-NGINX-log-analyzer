package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.BufferedReader;
import java.io.StringReader;
import java.time.OffsetDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class LogAnalyzerImplTest {
    @Mock
    private LogParserImpl logParser;

    private LogParserImpl logParserMock;

    @InjectMocks
    private LogAnalyzerImpl logAnalyzer;

    @BeforeEach
    void setUp() {
        logAnalyzer = new LogAnalyzerImpl();
        logParserMock = Mockito.mock(LogParserImpl.class);
    }

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
    void testAddIfInRangeIndirectlyThroughReadLogs() throws Exception {
        OffsetDateTime time = OffsetDateTime.now();
        LogRecord record = new LogRecord(time, "127.0.0.1", "/resource", 200, 500L, "-", "-");

        when(logParserMock.parseLine(anyString())).thenReturn(Optional.of(record));

        logAnalyzer.setFrom(time.minusHours(1));
        logAnalyzer.setTo(time.plusHours(1));

        try (BufferedReader reader = new BufferedReader(new StringReader("лог строка теста"))) {
            logAnalyzer.readLogs("http://example.com");

            assertEquals(1, logAnalyzer.logRecords().size());
            assertEquals(record, logAnalyzer.logRecords().get(0));
        }
    }

//    @Test
//    void testGenerateReport() {
//        LogRecord record1 = new LogRecord(OffsetDateTime.now(), "127.0.0.1", "/resource1", 200, 500L, "-", "-");
//        LogRecord record2 = new LogRecord(OffsetDateTime.now(), "127.0.0.1", "/resource2", 404, 1000L, "-", "-");
//
//        logAnalyzer.addIfInRange(record1);
//        logAnalyzer.addIfInRange(record2);
//
//        Report report = logAnalyzer.generateReport();
//
//        assertEquals(2, report.getTotalRequests());
//        assertEquals(2, report.getResourceCount().size());
//        assertEquals(2, report.getStatusCount().size());
//        assertEquals(750.0, report.getAverageResponseSize());
//        assertEquals(1000L, report.getPercentile95ResponseSize());
//    }
}
