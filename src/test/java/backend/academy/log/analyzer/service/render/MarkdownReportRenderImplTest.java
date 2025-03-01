package backend.academy.log.analyzer.service.render;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.model.SettingsReport;
import backend.academy.log.analyzer.service.render.impl.MarkdownReportRenderImpl;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkdownReportRenderImplTest {
    @Mock
    private MarkdownReportRenderImpl renderer;

    @Test
    void testRenderReportAsStringShouldGenerateCorrectMarkdown() {
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

        when(renderer.renderReportAsString(report)).thenReturn(
            "# All information\n" +
                "### General information \n\n" +
                "|        Parameter        |   Value   |\n" +
                "| resource1 | 10 |\n" +
                "| 200 | 30 |\n"
        );

        String result = renderer.renderReportAsString(report);

        assertNotNull(result, "The generated report should not be null or empty.");

        assertTrue(result.contains("# All information\n"), "The report should contain 'All information' section.");
        assertTrue(result.contains("### General information \n\n"),
            "The report should contain 'General information' section.");
        assertTrue(result.contains("|        Parameter        |   Value   |\n"),
            "The settings table should have correct header.");
        assertTrue(result.contains("resource1"), "The report should contain resource name.");
        assertTrue(result.contains("200"), "The report should contain HTTP status code.");

        verify(renderer, times(1)).renderReportAsString(report);
    }
}
