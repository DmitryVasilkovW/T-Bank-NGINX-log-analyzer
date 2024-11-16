package backend.academy.log.analyzer.service;

import backend.academy.log.analyzer.model.Report;

public interface LogAnalyzer {
    Report generateReport(String path, String filtrationMetric, String valueToFilter) throws Exception;
}
