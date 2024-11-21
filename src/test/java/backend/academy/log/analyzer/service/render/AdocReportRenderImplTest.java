package backend.academy.log.analyzer.service.render;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.model.SettingsReport;
import backend.academy.log.analyzer.service.render.impl.AdocReportRenderImpl;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdocReportRenderImplTest {
    @Mock
    private AdocReportRenderImpl reportRender;

    @Test
    void testRenderReportAsStringShouldGenerateCorrectAdoc() {
        Map<String, Long> resourceCount = Map.of("resource1", 10L, "resource2", 20L);
        Map<Integer, Long> statusCount = Map.of(200, 30L, 404, 5L);
        Map<String, Long> ipAddresses = Map.of("192.168.1.1", 15L, "192.168.1.2", 25L);
        Map<String, Long> userAgents = Map.of("Mozilla", 35L, "Chrome", 10L);

        SettingsReport settingsReport = new SettingsReport(
            OffsetDateTime.now(),
            OffsetDateTime.now().plusHours(1),
            List.of("source1", "source2"),
            "/path",
            new Pair<>("filter", "value")
        );

        Report report = new Report(
            50L,
            resourceCount,
            statusCount,
            200.5,
            1500L,
            ipAddresses,
            userAgents,
            settingsReport
        );

        when(reportRender.renderReportAsString(report)).thenReturn(
            "== All information\n"
                + "== Compilations\n"
                + "| Resource | Amount\n"
                + "| Code | Description | Amount"
        );

        String result = reportRender.renderReportAsString(report);

        assertTrue(result.contains("== All information"));
        assertTrue(result.contains("== Compilations"));
        assertTrue(result.contains("| Resource | Amount"));
        assertTrue(result.contains("| Code | Description | Amount"));

        verify(reportRender, times(1)).renderReportAsString(report);
    }
}
