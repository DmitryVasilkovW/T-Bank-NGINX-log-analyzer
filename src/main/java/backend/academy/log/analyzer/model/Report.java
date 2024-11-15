package backend.academy.log.analyzer.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record Report(
    long totalRequests,
    Map<String, Long> resourceCount,
    Map<Integer, Long> statusCount,
    double averageResponseSize,
    long percentile95ResponseSize,
    OffsetDateTime dateFrom,
    OffsetDateTime dateTo,
    List<String> sources,
    String path
) {
}
