package backend.academy.log.analyzer.service.impl;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

public class LogAnalyzerImpl implements LogAnalyzer {
    private final LogParserImpl logParser = new LogParserImpl();
    @Getter
    private final List<LogRecord> logRecords = new ArrayList<>();
    @Getter
    private OffsetDateTime from;
    @Getter
    private OffsetDateTime to;

    public void setFrom(OffsetDateTime from) {
        this.from = from;
    }

    public void setTo(OffsetDateTime to) {
        this.to = to;
    }

    @Override
    public void readLogs(String path) throws Exception {
        if (path.startsWith("http")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(path).openStream()))) {
                reader.lines().map(logParser::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(this::addIfInRange);
            }
        } else {
            Files.walk(Path.of(path)).filter(Files::isRegularFile).forEach(p -> {
                try (BufferedReader reader = Files.newBufferedReader(p)) {
                    reader.lines().map(logParser::parseLine)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(this::addIfInRange);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void addIfInRange(LogRecord record) {
        if ((from == null || !record.getTimeLocal().isBefore(from)) &&
            (to == null || !record.getTimeLocal().isAfter(to))) {
            logRecords.add(record);
        }
    }

    @Override
    public Report generateReport() {
        long totalRequests = logRecords.size();

        Map<String, Long> resourceCount = logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::getResource, Collectors.counting()));

        Map<Integer, Long> statusCount = logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::getStatusCode, Collectors.counting()));

        double averageResponseSize = logRecords.stream()
            .mapToLong(LogRecord::getResponseSize)
            .average()
            .orElse(0);

        List<Long> sortedResponseSizes = logRecords.stream()
            .map(LogRecord::getResponseSize)
            .sorted()
            .collect(Collectors.toList());

        long percentile95ResponseSize = sortedResponseSizes.isEmpty() ? 0 :
            sortedResponseSizes.get((int) (0.95 * sortedResponseSizes.size()));

        return new Report(totalRequests, resourceCount, statusCount, averageResponseSize, percentile95ResponseSize);
    }

}


