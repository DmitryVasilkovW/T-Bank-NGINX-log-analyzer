package backend.academy.log.analyzer.service.parser;

import backend.academy.log.analyzer.model.LogRecord;
import java.util.Optional;

public interface LogParser {
    Optional<LogRecord> parseLine(String line);
}
