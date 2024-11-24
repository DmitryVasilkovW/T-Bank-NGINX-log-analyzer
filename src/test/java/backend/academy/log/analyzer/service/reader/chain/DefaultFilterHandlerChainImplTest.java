package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.service.reader.chain.impl.DefaultFilterHandlerChainImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DefaultFilterHandlerChainImplTest {

    @Mock
    private FilterRequest mockRequest;

    @InjectMocks
    private DefaultFilterHandlerChainImpl handler;

    @Test
    void shouldReturnTrue() {
        boolean result = handler.handle(mockRequest);

        assertTrue(result);
    }
}
