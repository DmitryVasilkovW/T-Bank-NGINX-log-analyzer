package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.Report;
import java.time.OffsetDateTime;

public interface LogAnalyzer {
    Report generateReport(String path, String filtrationMetric, String valueToFilter) throws Exception;

    void setTimeFrom(OffsetDateTime from);

    void setTimeTo(OffsetDateTime to);
}
