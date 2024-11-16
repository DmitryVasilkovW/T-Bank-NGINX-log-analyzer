package backend.academy.log.analyzer.service.reader.impl;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.parser.LogParser;
import backend.academy.log.analyzer.service.reader.LogReader;
import backend.academy.log.analyzer.service.reader.chain.FilterHandlerChain;
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

public class LogReaderImpl implements LogReader {
    private final LogParser logParser;
    private final FilterHandlerChain filterHandlerChain;
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

    private static final String HTTP_PROTOCOL = "http";

    public LogReaderImpl(LogParser logParser, FilterHandlerChain filterHandlerChain) {
        this.logParser = logParser;
        this.filterHandlerChain = filterHandlerChain;
    }

    @Override
    public List<LogRecord> readLogs(String path, String filtrationMetric, String valueToFilter) throws Exception {
        setFiltration(filtrationMetric, valueToFilter);

        if (path.startsWith(HTTP_PROTOCOL)) {
            readHttp(path);
        } else if (containsGlobSymbols(path)) {
            readLocalSourceWithGlob(path);
        } else {
            readLocalSource(path);
        }

        return logRecords;
    }

    private void setFiltration(String filtrationMetric, String valueToFilter) {
        if (!filtrationMetric.isEmpty() && !valueToFilter.isEmpty()) {
            this.filtration = new Pair<>(filtrationMetric, valueToFilter);
        }
    }

    private void readHttp(String path) throws Exception {
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
    }

    private void readLocalSourceWithGlob(String path) throws Exception {
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
    }

    private void readLocalSource(String path) throws Exception {
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
        Optional<Pair<String, String>> filtrationO;
        if (filtration != null) {
            filtrationO = Optional.of(filtration);
        } else {
            filtrationO = Optional.empty();
        }

        return filterHandlerChain.handle(new FilterRequest(logRecord, filtrationO));
    }
}
