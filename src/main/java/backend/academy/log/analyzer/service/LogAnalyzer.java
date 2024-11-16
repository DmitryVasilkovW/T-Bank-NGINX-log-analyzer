package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.Report;

public interface LogAnalyzer {
    void readLogs(String path, String filtrationMetric, String valueToFilter) throws Exception;

    Report generateReport();
}
