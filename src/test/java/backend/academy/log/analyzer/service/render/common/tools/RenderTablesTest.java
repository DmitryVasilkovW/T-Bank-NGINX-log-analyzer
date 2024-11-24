package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.model.SettingsReport;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RenderTablesTest {

    @Test
    void renderSettingsTableTest() {
        OffsetDateTime dateFrom = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime dateTo = OffsetDateTime.parse("2024-01-31T23:59:59Z");
        List<String> sources = List.of("source1", "source2");
        Pair<String, String> filtration = new Pair<>("filterKey", "filterValue");

        SettingsReport settingsReport = new SettingsReport(
            dateFrom,
            dateTo,
            sources,
            "/api/test",
            filtration
        );

        Report report = new Report(
            0,
            Map.of(),
            Map.of(),
            0.0,
            0,
            Map.of(),
            Map.of(),
            settingsReport
        );

        StringBuilder sb = new StringBuilder();
        String title = "Settings Report\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%s: %s\n";

        RenderTables.renderSettingsTable(sb, report, title, header, separator, format);

        String result = sb.toString();
        assertTrue(result.contains("Path: /api/test"));
        assertTrue(result.contains("Sources: source1, source2"));
        assertTrue(result.contains("Start time: 2024-01-01T00:00Z\n"));
        assertTrue(result.contains("End time: 2024-01-31T23:59:59Z"));
        assertTrue(result.contains("Filtration: In filterKey parameter to show only filterValue"));
    }

    @Test
    void renderGeneralInfoTableTest() {
        Report report = Mockito.mock(Report.class);
        Mockito.when(report.totalRequests()).thenReturn(100L);
        Mockito.when(report.averageResponseSize()).thenReturn(512.0);
        Mockito.when(report.percentile95ResponseSize()).thenReturn(1024L);

        StringBuilder sb = new StringBuilder();
        String title = "General Info\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%s: %s\n";

        RenderTables.renderGeneralInfoTable(sb, report, title, header, separator, format);

        String result = sb.toString();
        assertTrue(result.contains("Number of requests: 100\n"));
        assertTrue(result.contains("Average response size: 512.0b\n"));
        assertTrue(result.contains("95p response size: 1024b"));
    }

    @Test
    void renderResourcesTableTest() {
        Map<String, Long> resourceCount = Map.of(
            "resource1", 50L,
            "resource2", 30L
        );

        Report report = Mockito.mock(Report.class);
        Mockito.when(report.resourceCount()).thenReturn(resourceCount);

        StringBuilder sb = new StringBuilder();
        String title = "Resources\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%s: %d\n";

        RenderTables.renderResourcesTable(sb, report, title, header, separator, format);

        String result = sb.toString();
        assertTrue(result.contains("resource1: 50"));
        assertTrue(result.contains("resource2: 30"));
    }

    @Test
    void renderRequestsCodeTableTest() {
        Map<Integer, Long> statusCount = Map.of(
            200, 120L,
            404, 20L
        );

        Report report = Mockito.mock(Report.class);
        Mockito.when(report.statusCount()).thenReturn(statusCount);

        Mockito.mockStatic(HttpStatusDescription.class);
        Mockito.when(HttpStatusDescription.getStatusDescription(200)).thenReturn("OK");
        Mockito.when(HttpStatusDescription.getStatusDescription(404)).thenReturn("Not Found");

        StringBuilder sb = new StringBuilder();
        String title = "Request Codes\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%d: %s (%d times)\n";

        RenderTables.renderRequestsCodeTable(sb, report, title, header, separator, format);

        String result = sb.toString();
        assertTrue(result.contains("200: OK (120 times)"));
        assertTrue(result.contains("404: Not Found (20 times)"));

        Mockito.clearAllCaches();
    }

}
