package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.model.SettingsReport;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RenderTablesTest {

    @Mock
    private Report report;

    @Mock
    private SettingsReport settingsReport;

    @Mock
    private StringBuilder sb;

    @Test
    void renderSettingsTableTest() {
        OffsetDateTime dateFrom = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime dateTo = OffsetDateTime.parse("2024-01-31T23:59:59Z");
        List<String> sources = List.of("source1", "source2");
        Pair<String, String> filtration = new Pair<>("filterKey", "filterValue");

        Mockito.when(settingsReport.dateFrom()).thenReturn(dateFrom);
        Mockito.when(settingsReport.dateTo()).thenReturn(dateTo);
        Mockito.when(settingsReport.sources()).thenReturn(sources);
        Mockito.when(settingsReport.path()).thenReturn("/api/test");
        Mockito.when(settingsReport.filtration()).thenReturn(filtration);
        Mockito.when(report.settingsReport()).thenReturn(settingsReport);

        String title = "Settings Report\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%s: %s\n";

        RenderTables.renderSettingsTable(sb, report, title, header, separator, format);

        Mockito.verify(sb).append(Mockito.contains("Path: /api/test"));
        Mockito.verify(sb).append(Mockito.contains("Sources: source1, source2"));
        Mockito.verify(sb).append(Mockito.contains("Start time: 2024-01-01T00:00Z\n"));
        Mockito.verify(sb).append(Mockito.contains("End time: 2024-01-31T23:59:59Z"));
        Mockito.verify(sb).append(Mockito.contains("Filtration: In filterKey parameter to show only filterValue"));
    }

    @Test
    void renderGeneralInfoTableTest() {
        Mockito.when(report.totalRequests()).thenReturn(100L);
        Mockito.when(report.averageResponseSize()).thenReturn(512.0);
        Mockito.when(report.percentile95ResponseSize()).thenReturn(1024L);

        String title = "General Info\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%s: %s\n";

        RenderTables.renderGeneralInfoTable(sb, report, title, header, separator, format);

        Mockito.verify(sb).append(Mockito.contains("Number of requests: 100\n"));
        Mockito.verify(sb).append(Mockito.contains("Average response size: 512.0b\n"));
        Mockito.verify(sb).append(Mockito.contains("95p response size: 1024b"));
    }

    @Test
    void renderResourcesTableTest() {
        Map<String, Long> resourceCount = Map.of(
            "resource1", 50L,
            "resource2", 30L
        );

        Mockito.when(report.resourceCount()).thenReturn(resourceCount);

        String title = "Resources\n";
        String header = "--------------------\n";
        String separator = "\n";
        String format = "%s: %d\n";

        RenderTables.renderResourcesTable(sb, report, title, header, separator, format);

        Mockito.verify(sb).append(Mockito.contains("resource1: 50"));
        Mockito.verify(sb).append(Mockito.contains("resource2: 30"));
    }

    @Test
    void renderRequestsCodeTableTest() {
        Map<Integer, Long> statusCount = Map.of(
            200, 120L,
            404, 20L
        );

        Mockito.when(report.statusCount()).thenReturn(statusCount);

        try (var mockedStatic = Mockito.mockStatic(HttpStatusDescription.class)) {
            mockedStatic.when(() -> HttpStatusDescription.getStatusDescription(200)).thenReturn("OK");
            mockedStatic.when(() -> HttpStatusDescription.getStatusDescription(404)).thenReturn("Not Found");

            String title = "Request Codes\n";
            String header = "--------------------\n";
            String separator = "\n";
            String format = "%d: %s (%d times)\n";

            RenderTables.renderRequestsCodeTable(sb, report, title, header, separator, format);

            Mockito.verify(sb).append(Mockito.contains("200: OK (120 times)"));
            Mockito.verify(sb).append(Mockito.contains("404: Not Found (20 times)"));
        }
    }
}
