package backend.academy.log.analyzer.model;

import java.util.Map;

public record Report(
    long totalRequests,
    Map<String, Long> resourceCount,
    Map<Integer, Long> statusCount,
    double averageResponseSize,
    long percentile95ResponseSize,
    Map<String, Long> ipAddresses,
    Map<String, Long> userAgents,
    SettingsReport settingsReport
) {
}
