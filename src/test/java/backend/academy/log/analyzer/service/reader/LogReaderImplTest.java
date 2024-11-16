package backend.academy.log.analyzer.service.reader;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.service.parser.LogParser;
import backend.academy.log.analyzer.service.reader.chain.FilterHandlerChain;
import backend.academy.log.analyzer.service.reader.impl.LogReaderImpl;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class LogReaderImplTest {
    @Mock
    private LogParser logParser;

    @Mock
    private FilterHandlerChain filterHandlerChain;

    @InjectMocks
    private LogReaderImpl logReader;

    @Test
    void testReadLogsInvalidPathShouldThrowException() {
        String invalidPath = "invalid://path";
        String filtrationMetric = "status";
        String valueToFilter = "404";

        assertThrows(IllegalArgumentException.class, () -> {
            logReader.readLogs(invalidPath, filtrationMetric, valueToFilter);
        });
    }

    @Test
    void testReadLogsInvalidGlobPatternShouldThrowException() {
        String invalidGlobPattern = "src/test/resources/#.log";
        String filtrationMetric = "method";
        String valueToFilter = "POST";

        assertThrows(IllegalArgumentException.class, () -> {
            logReader.readLogs(invalidGlobPattern, filtrationMetric, valueToFilter);
        });
    }

    @Test
    void testReadLogsValidLocalFileShouldReadLogs() throws Exception {
        String path = "src/test/resources";
        String filtrationMetric = "status";
        String valueToFilter = "200";

        OffsetDateTime time = OffsetDateTime.now();
        LogRecord logRecord =
            new LogRecord(time, "192.168.1.1", "/home", 200, 1024, "http://referrer", "Mozilla", "GET");
        Mockito.when(logParser.parseLine(Mockito.anyString())).thenReturn(Optional.of(logRecord));
        Mockito.when(filterHandlerChain.handle(Mockito.any(FilterRequest.class))).thenReturn(true);

        List<LogRecord> result = logReader.readLogs(path, filtrationMetric, valueToFilter);

        assertNotNull(result, "Result should not be null");
        assertEquals(32, result.size(), "Should have one log record");
        assertTrue(result.contains(logRecord), "Log record should be in the result");
    }
}
