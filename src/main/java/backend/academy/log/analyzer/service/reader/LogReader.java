package backend.academy.log.analyzer.service.reader;

import backend.academy.log.analyzer.model.LogRecord;
import java.util.List;

public interface LogReader {
    List<LogRecord> readLogs(String path, String filtrationMetric, String valueToFilter) throws Exception;
}
