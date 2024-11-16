package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.factory.impl.LogAnalyzerFactoryImpl;
import backend.academy.log.analyzer.service.reader.chain.factory.impl.FilterHandlerChainFactoryImpl;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogAnalyzerImplTest {
    private final LogAnalyzer realAnalyzer =
        new LogAnalyzerFactoryImpl(
            new FilterHandlerChainFactoryImpl()).create();

    @Test
    void generateReportWithValidDataShouldReturnCorrectReport() throws Exception {
        realAnalyzer.setTimeFrom(LocalDate.parse("2014-08-05").atStartOfDay().atOffset(ZoneOffset.UTC));
        realAnalyzer.setTimeTo(LocalDate.parse("2017-08-05").atStartOfDay().atOffset(ZoneOffset.UTC));

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
        assertThrows(IllegalArgumentException.class, () -> realAnalyzer.generateReport("invalid/path", "", ""));
    }

    @Test
    void generateReportShouldHandleEmptyLogList() throws Exception {
        Report report = realAnalyzer.generateReport("src/test/resources/empty.log", "resource", "GET");

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
        Report report = realAnalyzer.generateReport("src/test/resources/logs.log", "resource", "/aaaaaaaaaaa");

        assertNotNull(report);
        assertEquals(0, report.totalRequests());
    }
}

