package backend.academy.log.analyzer.service.impl;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.model.SettingsReport;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

public class LogAnalyzerImpl implements LogAnalyzer {
    private final LogParserImpl logParser;

    @Getter
    private final List<LogRecord> logRecords = new ArrayList<>();
    private final List<String> sources = new ArrayList<>();

    @Setter
    @Getter
    private OffsetDateTime from;

    @Setter
    @Getter
    private OffsetDateTime to;

    private Pair<String, String> filtration;
    private static final Double COEFFICIENT = 0.95;

    public LogAnalyzerImpl(LogParserImpl logParser) {
        this.logParser = logParser;
    }

    @Override
    public Report generateReport(String path, String filtrationMetric, String valueToFilter) throws Exception {
        readLogs(path, filtrationMetric, valueToFilter);

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

    private void readLogs(String path, String filtrationMetric, String valueToFilter) throws Exception {
        if (!filtrationMetric.isEmpty() && !valueToFilter.isEmpty()) {
            this.filtration = new Pair<>(filtrationMetric, valueToFilter);
        }

        if (path.startsWith("http")) {
            String fileName = extractFileNameFromURL(path);
            sources.add(fileName);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(path).openStream()))) {
                reader.lines()
                    .map(logParser::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(this::matchesFilter)
                    .forEach(this::addIfInRange);
            }
        } else if (containsGlobSymbols(path)) {
            Path basePath = Path.of(path).getParent();
            String globPattern = "glob:" + Path.of(path).getFileName();
            PathMatcher matcher = basePath.getFileSystem().getPathMatcher(globPattern);

            Files.walk(basePath)
                .filter(Files::isRegularFile)
                .filter(p -> matcher.matches(p.getFileName()))
                .forEach(p -> {
                    sources.add(p.getFileName().toString());
                    processFile(p);
                });
        } else {
            Path regularPath = Path.of(path);

            if (Files.isRegularFile(regularPath)) {
                sources.add(regularPath.getFileName().toString());
                processFile(regularPath);
            } else if (Files.isDirectory(regularPath)) {
                Files.walk(regularPath)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        sources.add(p.getFileName().toString());
                        processFile(p);
                    });
            } else {
                throw new IllegalArgumentException(
                    "Path is neither a valid file, directory, nor a glob pattern: " + path);
            }
        }
    }

    private String extractFileNameFromURL(String url) {
        try {
            return Path.of(new URL(url).getPath()).getFileName().toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }
    }

    private void processFile(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.lines()
                .map(logParser::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(this::matchesFilter)
                .forEach(this::addIfInRange);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean containsGlobSymbols(String path) {
        return path.contains("*") || path.contains("?") || path.contains("{") || path.contains("}") ||
            path.contains("[") || path.contains("]");
    }

    private void addIfInRange(LogRecord record) {
        if ((from == null || !record.timeLocal().isBefore(from)) &&
            (to == null || !record.timeLocal().isAfter(to))) {
            logRecords.add(record);
        }
    }

    private boolean matchesFilter(LogRecord record) {
        if (filtration == null) {
            return true;
        }

        String metric = filtration.first();
        String value = filtration.second();

        switch (metric.toLowerCase()) {
            case "agent":
                return record.httpUserAgent() != null && record.httpUserAgent().startsWith(value);
            case "method":
                return record.method() != null && record.method().equalsIgnoreCase(value);
            case "resource":
                return record.resource() != null && record.resource().contains(value);
            case "status":
                try {
                    int statusCode = Integer.parseInt(value);
                    return record.statusCode() == statusCode;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "ip":
                return record.remoteAddr() != null && record.remoteAddr().equals(value);
            default:
                return true;
        }
    }

}


