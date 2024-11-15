package backend.academy.log.analyzer.service.rander;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.impl.AdocReportRanderImpl;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdocReportRanderImplTest {
    private final ReportRander rander = new AdocReportRanderImpl();

    @Test
    void testFormatReportAsAdoc() {
        Map<String, Long> resourceCount = new HashMap<>();
        resourceCount.put("/index.html", 10L);
        Map<Integer, Long> statusCount = new HashMap<>();
        statusCount.put(200, 8L);
        statusCount.put(404, 2L);

        Report report = new Report(
            10,
            resourceCount,
            statusCount,
            512.0,
            1000L,
            null,
            null
        );
        String adocReport = rander.randerReportAsString(report);

        assertTrue(adocReport.contains("== Общая информация"));
        assertTrue(adocReport.contains("| Количество запросов | 10"));
        assertTrue(adocReport.contains("| 95p размера ответа | 1000b"));
    }
}
