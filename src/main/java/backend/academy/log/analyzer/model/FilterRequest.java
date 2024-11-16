package backend.academy.log.analyzer.model;

import java.util.Optional;

public record FilterRequest(
    LogRecord logRecord,
    Optional<Pair<String, String>> filtration
) {
}
