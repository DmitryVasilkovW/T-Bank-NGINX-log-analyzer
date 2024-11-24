package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.reader.chain.impl.AgentFilterHandlerChainImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @Mock
    private LogRecord logRecord;

    @InjectMocks
    private AgentFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrueWhenAgentMatches() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("agent", "sample")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.httpUserAgent()).thenReturn("sample");

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).httpUserAgent();
    }

    @Test
    void shouldReturnFalseWhenAgentDoesNotMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("agent", "sample")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.httpUserAgent()).thenReturn("239sample239");

        boolean result = handler.handle(mockRequest);

        assertFalse(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).httpUserAgent();
    }
}
