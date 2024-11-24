package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.reader.chain.impl.ResourceFilterHandlerChainImpl;
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
class ResourceFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @Mock
    private LogRecord logRecord;

    @InjectMocks
    private ResourceFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrueWhenResourceMatches() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("resource", "ast.ll")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.resource()).thenReturn("ast.ll");

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).resource();
    }

    @Test
    void shouldReturnFalseWhenResourceDoesNotMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("resource", "lexer.cpp")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.resource()).thenReturn("ast.ll");

        boolean result = handler.handle(mockRequest);

        assertFalse(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).resource();
    }
}
