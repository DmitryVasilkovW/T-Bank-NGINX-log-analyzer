package backend.academy.log.analyzer.model;

import java.time.OffsetDateTime;
import java.util.List;

public record SettingsReport(
    OffsetDateTime dateFrom,
    OffsetDateTime dateTo,
    List<String> sources,
    String path,
    Pair<String, String> filtration
) {
}
