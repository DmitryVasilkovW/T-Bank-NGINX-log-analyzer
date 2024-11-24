package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.service.reader.chain.impl.NoFilterHandlerChainImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @InjectMocks
    private NoFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrue() {
        when(mockRequest.filtration()).thenReturn(Optional.empty());

        boolean result = handler.handle(mockRequest);

        assertTrue(result);
    }
}
