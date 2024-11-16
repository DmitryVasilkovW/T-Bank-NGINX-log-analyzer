package backend.academy.log.analyzer.service.reader;

import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.parser.LogParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

public class LogReaderImpl {
    private final LogParser logParser;
    private final List<LogRecord> logRecords = new ArrayList<>();

    @Getter
    private final List<String> sources = new ArrayList<>();

    @Setter
    @Getter
    private OffsetDateTime from;

    @Setter
    @Getter
    private OffsetDateTime to;

    @Getter
    private Pair<String, String> filtration;

    public LogReaderImpl(LogParser logParser) {
        this.logParser = logParser;
    }

    public List<LogRecord> readLogs(String path, String filtrationMetric, String valueToFilter) throws Exception {
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

        return logRecords;
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
        return path.contains("*")
            || path.contains("?")
            || path.contains("{")
            || path.contains("}")
            || path.contains("[")
            || path.contains("]");
    }

    private void addIfInRange(LogRecord logRecord) {
        if ((from == null || !logRecord.timeLocal().isBefore(from))
            && (to == null || !logRecord.timeLocal().isAfter(to))) {
            logRecords.add(logRecord);
        }
    }

    private boolean matchesFilter(LogRecord logRecord) {
        if (filtration == null) {
            return true;
        }

        String metric = filtration.first();
        String value = filtration.second();

        switch (metric.toLowerCase()) {
            case "agent":
                return logRecord.httpUserAgent() != null && logRecord.httpUserAgent().startsWith(value);
            case "method":
                return logRecord.method() != null && logRecord.method().equalsIgnoreCase(value);
            case "resource":
                return logRecord.resource() != null && logRecord.resource().contains(value);
            case "status":
                try {
                    int statusCode = Integer.parseInt(value);
                    return logRecord.statusCode() == statusCode;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "ip":
                return logRecord.remoteAddr() != null && logRecord.remoteAddr().equals(value);
            default:
                return true;
        }
    }
}
