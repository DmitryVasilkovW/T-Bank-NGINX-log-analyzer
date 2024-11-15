package backend.academy.log.analyzer.service.rander;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.impl.MarkdownReportRanderImpl;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarkdownReportRanderImplTest {
    private final ReportRander rander = new MarkdownReportRanderImpl();

    @Test
    void testFormatReportAsMarkdown() {
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
            null,
            null,
            null,
            null,
            null
        );
        String markdownReport = rander.renderReportAsString(report);

        assertTrue(markdownReport.contains("#### General information"));
        assertTrue(markdownReport.contains("|  Number of requests  | 10 |"));
        assertTrue(markdownReport.contains("|  95p response size  | 1000b |"));
    }
}
