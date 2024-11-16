package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogAnalyzerImplTest {

    @Mock
    private LogParserImpl logParser;

    @InjectMocks
    private LogAnalyzerImpl logAnalyzer;

    private final LogAnalyzerImpl realAnalyzer = new LogAnalyzerImpl(new LogParserImpl());

    @Test
    void generateReportWithValidDataShouldReturnCorrectReport() throws Exception {
        realAnalyzer.setFrom(LocalDate.parse("2014-08-05").atStartOfDay().atOffset(ZoneOffset.UTC));
        realAnalyzer.setTo(LocalDate.parse("2017-08-05").atStartOfDay().atOffset(ZoneOffset.UTC));

        Report report = realAnalyzer.generateReport("src/test/resources/logs.log", "", "");

        assertNotNull(report);
        assertEquals(32, report.totalRequests());
        assertEquals(21, report.resourceCount().get("/downloads/product_1"));
        assertEquals(5, report.statusCount().get(200));
        assertEquals(314.2, report.averageResponseSize(), 0.1);
        assertEquals(3301, report.percentile95ResponseSize());
        assertTrue(report.ipAddresses().containsKey("188.138.60.101"));
    }

    @Test
    void generateReportShouldThrowException_whenInvalidPath() {
        assertThrows(IllegalArgumentException.class, () -> logAnalyzer.generateReport("invalid/path", "", ""));
    }

    @Test
    void generateReportShouldHandleEmptyLogList() throws Exception {
        when(logParser.parseLine(anyString())).thenReturn(Optional.empty());

        Report report = logAnalyzer.generateReport("src/test/resources/logs.log", "resource", "GET");

        assertNotNull(report);
        assertEquals(0, report.totalRequests());
    }

    @Test
    void generateReportShouldHandleFilter() throws Exception {
        Report report = realAnalyzer.generateReport("src/test/resources/logs.log", "resource", "/downloads/product_1");

        assertNotNull(report);
        assertEquals(21, report.totalRequests());
        assertTrue(report.resourceCount().containsKey("/downloads/product_1"));
        assertFalse(report.resourceCount().containsKey("/downloads/product_2"));
    }

    @Test
    void generateReportShouldHandleNoMatchingLogs() throws Exception {
        LogRecord log1 = mock(LogRecord.class);
        when(log1.resource()).thenReturn("/contact");

        logAnalyzer.setFrom(OffsetDateTime.now().minusDays(1));
        logAnalyzer.setTo(OffsetDateTime.now());

        when(logParser.parseLine(anyString())).thenReturn(Optional.of(log1));

        Report report = logAnalyzer.generateReport("src/test/resources/logs.log", "resource", "/about");

        assertNotNull(report);
        assertEquals(0, report.totalRequests());
    }
}

