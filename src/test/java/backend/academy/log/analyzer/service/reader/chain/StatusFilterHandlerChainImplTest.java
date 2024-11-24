package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.reader.chain.impl.StatusFilterHandlerChainImpl;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @Mock
    private LogRecord logRecord;

    @InjectMocks
    private StatusFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrueWhenStatusMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("status", "200")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(mockRequest.logRecord().statusCode()).thenReturn(200);

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
        verify(mockRequest, times(2)).filtration();
        verify(mockRequest.logRecord(), times(1)).statusCode();
    }

    @Test
    void shouldReturnFalseWhenStatusDoesNotMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("status", "404")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(mockRequest.logRecord().statusCode()).thenReturn(200);

        boolean result = handler.handle(mockRequest);

        assertFalse(result);
        verify(mockRequest, times(2)).filtration();
        verify(mockRequest.logRecord(), times(1)).statusCode();
    }

    @Test
    void shouldReturnFalseWhenFiltrationValueIsInvalid() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("status", "invalid")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(mockRequest.logRecord().statusCode()).thenReturn(200);

        boolean result = handler.handle(mockRequest);

        assertFalse(result);
        verify(mockRequest, times(2)).filtration();
        verifyNoMoreInteractions(mockRequest.logRecord());
    }
}
