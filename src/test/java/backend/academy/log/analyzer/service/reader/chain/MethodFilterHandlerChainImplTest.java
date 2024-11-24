package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.reader.chain.impl.MethodFilterHandlerChainImpl;
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
class MethodFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @Mock
    private LogRecord logRecord;

    @InjectMocks
    private MethodFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrueWhenMethodMatchWithDifferentCases() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("method", "GET")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.method()).thenReturn("get");

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).method();
    }

    @Test
    void shouldReturnTrueWhenMethodMatchWithSameCases() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("method", "GET")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.method()).thenReturn("GET");

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).method();
    }

    @Test
    void shouldReturnFalseWhenMethodDoesNotMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("method", "GET")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.method()).thenReturn("POST");

        boolean result = handler.handle(mockRequest);

        assertFalse(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).method();
    }
}
