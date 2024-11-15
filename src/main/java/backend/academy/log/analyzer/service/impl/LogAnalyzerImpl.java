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
import java.nio.file.PathMatcher;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

public class LogAnalyzerImpl implements LogAnalyzer {
    private final LogParserImpl logParser;
    @Getter
    private final List<LogRecord> logRecords = new ArrayList<>();
    @Getter
    private OffsetDateTime from;
    @Getter
    private OffsetDateTime to;

    public LogAnalyzerImpl(LogParserImpl logParser) {
        this.logParser = logParser;
    }

    public void setFrom(OffsetDateTime from) {
        this.from = from;
    }

    public void setTo(OffsetDateTime to) {
        this.to = to;
    }

    private final List<String> sources = new ArrayList<>();
    private String path;

    @Override
    public void readLogs(String path) throws Exception {
        this.path = path;
        if (path.startsWith("http")) {
            String fileName = extractFileNameFromURL(path); // Извлекаем имя файла из URL
            sources.add(fileName); // Добавляем имя файла в список источников
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(path).openStream()))) {
                reader.lines().map(logParser::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
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
                    sources.add(p.getFileName().toString()); // Добавляем только имя файла
                    processFile(p);
                });
        } else {
            Path regularPath = Path.of(path);

            if (Files.isRegularFile(regularPath)) {
                sources.add(regularPath.getFileName().toString()); // Добавляем только имя файла
                processFile(regularPath);
            } else if (Files.isDirectory(regularPath)) {
                Files.walk(regularPath)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        sources.add(p.getFileName().toString()); // Добавляем только имя файла
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
            reader.lines().map(logParser::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
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

    @Override
    public Report generateReport() {
        long totalRequests = logRecords.size();

        Map<String, Long> resourceCount = logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::resource, Collectors.counting()));

        Map<Integer, Long> statusCount = logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::statusCode, Collectors.counting()));

        double averageResponseSize = logRecords.stream()
            .mapToLong(LogRecord::responseSize)
            .average()
            .orElse(0);

        List<Long> sortedResponseSizes = logRecords.stream()
            .map(LogRecord::responseSize)
            .sorted()
            .collect(Collectors.toList());

        long percentile95ResponseSize = sortedResponseSizes.isEmpty() ? 0 :
            sortedResponseSizes.get((int) (0.95 * sortedResponseSizes.size()));

        Map<String, Long> ipAddresses = logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::remoteAddr, Collectors.counting()));

        Map<String, Long> userAgents = logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::httpUserAgent, Collectors.counting()));


        return new Report(
            totalRequests,
            resourceCount,
            statusCount,
            averageResponseSize,
            percentile95ResponseSize,
            from,
            to,
            sources,
            path,
            ipAddresses,
            userAgents
        );
    }

}


