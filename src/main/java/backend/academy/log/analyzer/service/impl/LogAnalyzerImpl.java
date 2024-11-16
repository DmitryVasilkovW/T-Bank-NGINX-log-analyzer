package backend.academy.log.analyzer.service.impl;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.model.SettingsReport;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.reader.impl.LogReaderImpl;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

public class LogAnalyzerImpl implements LogAnalyzer {
    private final LogReaderImpl logReader;
    private List<String> sources = new ArrayList<>();
    private List<LogRecord> logRecords = new ArrayList<>();

    @Setter
    @Getter
    private OffsetDateTime from;

    @Setter
    @Getter
    private OffsetDateTime to;

    private Pair<String, String> filtration;
    private static final Double COEFFICIENT = 0.95;

    public LogAnalyzerImpl(LogReaderImpl logReader) {
        this.logReader = logReader;
    }

    @Override
    public Report generateReport(String path, String filtrationMetric, String valueToFilter) throws Exception {
        setReportParameters(path, filtrationMetric, valueToFilter);

        long totalRequests = logRecords.size();
        Map<String, Long> resourceCount = getMapOfAmount(LogRecord::resource);
        Map<Integer, Long> statusCount = getMapOfAmount(LogRecord::statusCode);
        Map<String, Long> ipAddresses = getMapOfAmount(LogRecord::remoteAddr);
        Map<String, Long> userAgents = getMapOfAmount(LogRecord::httpUserAgent);
        double averageResponseSize = getAverageResponseSize();
        List<Long> sortedResponseSizes = getSortedResponseSizes();
        long percentile95ResponseSize = getPercentile95ResponseSize(sortedResponseSizes);
        SettingsReport settingsReport = getSettingsReport(path);

        return new Report(
            totalRequests,
            resourceCount,
            statusCount,
            averageResponseSize,
            percentile95ResponseSize,
            ipAddresses,
            userAgents,
            settingsReport
        );
    }

    private void setReportParameters(String path, String filtrationMetric, String valueToFilter) throws Exception {
        logRecords = logReader.readLogs(path, filtrationMetric, valueToFilter);
        filtration = logReader.filtration();
        sources = logReader.sources();
    }

    private SettingsReport getSettingsReport(String path) {
        return new SettingsReport(
            from,
            to,
            sources,
            path,
            filtration
        );
    }

    private long getPercentile95ResponseSize(List<Long> sortedResponseSizes) {
        return sortedResponseSizes.isEmpty()
            ? 0 : sortedResponseSizes.get((int) (COEFFICIENT * sortedResponseSizes.size()));
    }

    private List<Long> getSortedResponseSizes() {
        return logRecords.stream()
            .map(LogRecord::responseSize)
            .sorted()
            .toList();
    }

    private double getAverageResponseSize() {
        return logRecords.stream()
            .mapToLong(LogRecord::responseSize)
            .average()
            .orElse(0);
    }

    private <K> Map<K, Long> getMapOfAmount(Function<LogRecord, K> keyExtractor) {
        return logRecords.stream()
            .collect(Collectors.groupingBy(keyExtractor, Collectors.counting()));
    }
}


