package backend.academy.log.analyzer.service.rander;

import org.junit.jupiter.api.Test;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.impl.ReportRanderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportRanderImplTest {

    @Test
    void testFormatReportAsMarkdown() {
        Map<String, Long> resourceCount = new HashMap<>();
        resourceCount.put("/index.html", 10L);
        Map<Integer, Long> statusCount = new HashMap<>();
        statusCount.put(200, 8L);
        statusCount.put(404, 2L);

        Report report = new Report(10, resourceCount, statusCount, 512.0, 1000L);
        String markdownReport = ReportRanderImpl.formatReport(report, "markdown");

        assertTrue(markdownReport.contains("#### Общая информация"));
        assertTrue(markdownReport.contains("| Количество запросов  | 10 |"));
        assertTrue(markdownReport.contains("| 95p размера ответа  | 1000b |"));
    }

    @Test
    void testFormatReportAsAdoc() {
        Map<String, Long> resourceCount = new HashMap<>();
        resourceCount.put("/index.html", 10L);
        Map<Integer, Long> statusCount = new HashMap<>();
        statusCount.put(200, 8L);
        statusCount.put(404, 2L);

        Report report = new Report(10, resourceCount, statusCount, 512.0, 1000L);
        String adocReport = ReportRanderImpl.formatReport(report, "adoc");

        assertTrue(adocReport.contains("== Общая информация"));
        assertTrue(adocReport.contains("| Количество запросов | 10"));
        assertTrue(adocReport.contains("| 95p размера ответа | 1000b"));
    }

    @Test
    void testUnsupportedFormat() {
        Map<String, Long> resourceCount = new HashMap<>();
        resourceCount.put("/index.html", 10L);
        Map<Integer, Long> statusCount = new HashMap<>();
        statusCount.put(200, 8L);
        statusCount.put(404, 2L);

        Report report = new Report(10, resourceCount, statusCount, 512.0, 1000L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ReportRanderImpl.formatReport(report, "xml");
        });

        assertEquals("Unsupported format: xml", exception.getMessage());
    }
}

