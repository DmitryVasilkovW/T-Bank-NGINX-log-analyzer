package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.reader.impl.LogReaderImpl;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogAnalyzerImplTest {

    @Mock
    private LogReaderImpl logReader;

    @InjectMocks
    private LogAnalyzerImpl realAnalyzer;

    @Test
    void testGenerateReportWithValidDataShouldReturnCorrectReport() throws Exception {
        List<LogRecord> mockLogs = Arrays.asList(
            new LogRecord(
                OffsetDateTime.now(),
                "188.138.60.101",
                "/downloads/product_1",
                200,
                1500,
                "",
                "Mozilla",
                "GET"
            ),
            new LogRecord(
                OffsetDateTime.now(),
                "188.138.60.101",
                "/downloads/product_1",
                200,
                1200,
                "",
                "Mozilla",
                "GET"
            ),
            new LogRecord(
                OffsetDateTime.now(),
                "192.168.0.1",
                "/downloads/product_2",
                200,
                1300,
                "",
                "Mozilla",
                "GET"
            )
        );

        when(logReader.readLogs(anyString(), anyString(), anyString())).thenReturn(mockLogs);

        realAnalyzer.setTimeFrom(OffsetDateTime.parse("2014-08-05T00:00:00Z"));
        realAnalyzer.setTimeTo(OffsetDateTime.parse("2017-08-05T00:00:00Z"));

        Report report = realAnalyzer.generateReport("src/test/resources/logs.log", "", "");

        assertNotNull(report);
        assertEquals(3, report.totalRequests());
        assertEquals(2, report.resourceCount().get("/downloads/product_1"));
        assertEquals(1, report.resourceCount().get("/downloads/product_2"));
        assertEquals(3, report.statusCount().get(200));
        assertEquals(1333.0, report.averageResponseSize(), 0.4);
    }

    @Test
    void testGenerateReportShouldThrowExceptionWhenInvalidPath() throws Exception {
        when(logReader.readLogs(anyString(), anyString(), anyString())).thenThrow(
            new IllegalArgumentException("Invalid path"));

        assertThrows(IllegalArgumentException.class, () -> realAnalyzer.generateReport("invalid/path", "", ""));
    }

    @Test
    void testGenerateReportShouldHandleEmptyLogList() throws Exception {
        when(logReader.readLogs(anyString(), anyString(), anyString())).thenReturn(List.of());

        Report report = realAnalyzer.generateReport("src/test/resources/empty.log", "resource", "GET");

        assertNotNull(report);
        assertEquals(0, report.totalRequests());
    }

    @Test
    void testGenerateReportShouldHandleFilter() throws Exception {
        List<LogRecord> mockLogs = Arrays.asList(
            new LogRecord(
                OffsetDateTime.now(),
                "188.138.60.101",
                "/downloads/product_1",
                200,
                1500,
                "",
                "Mozilla",
                "GET"
            ),
            new LogRecord(
                OffsetDateTime.now(),
                "192.168.0.1",
                "/downloads/product_1",
                200,
                1200,
                "",
                "Mozilla",
                "GET"
            )
        );

        when(logReader.readLogs(anyString(), anyString(), anyString())).thenReturn(mockLogs);

        Report report = realAnalyzer.generateReport("src/test/resources/logs.log",
            "resource", "/downloads/product_1");

        assertNotNull(report);
        assertEquals(2, report.totalRequests());
        assertTrue(report.resourceCount().containsKey("/downloads/product_1"));
        assertFalse(report.resourceCount().containsKey("/downloads/product_2"));
    }

    @Test
    void testGenerateReportShouldHandleNoMatchingLogs() throws Exception {
        when(logReader.readLogs(anyString(), anyString(), anyString())).thenReturn(List.of());

        Report report = realAnalyzer.generateReport("src/test/resources/logs.log",
            "resource", "/aaaaaaaaaaa!");

        assertNotNull(report);
        assertEquals(0, report.totalRequests());
    }
}
