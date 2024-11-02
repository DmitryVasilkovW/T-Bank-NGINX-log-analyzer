package backend.academy.log.analyzer;

import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class LogAnalyzer {
    private final LogParser logParser = new LogParser();
    private final List<LogRecord> logRecords = new ArrayList<>();
    private LocalDateTime from;
    private LocalDateTime to;

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

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


