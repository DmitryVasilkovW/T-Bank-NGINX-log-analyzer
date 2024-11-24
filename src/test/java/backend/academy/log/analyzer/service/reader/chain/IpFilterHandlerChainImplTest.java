package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.LogRecord;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.reader.chain.impl.IpFilterHandlerChainImpl;
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
class IpFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @Mock
    private LogRecord logRecord;

    @InjectMocks
    private IpFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrueWhenIpMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("ip", "173.203.139.108")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.remoteAddr()).thenReturn("173.203.139.108");

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).remoteAddr();
    }

    @Test
    void shouldReturnFalseWhenIpDoesNotMatch() {
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("ip", "173.203.139.108")));
        when(mockRequest.logRecord()).thenReturn(logRecord);
        when(logRecord.remoteAddr()).thenReturn("239.233.923.339");

        boolean result = handler.handle(mockRequest);

        assertFalse(result);
        verify(mockRequest, times(2)).filtration();
        verify(logRecord, times(1)).remoteAddr();
    }
}
