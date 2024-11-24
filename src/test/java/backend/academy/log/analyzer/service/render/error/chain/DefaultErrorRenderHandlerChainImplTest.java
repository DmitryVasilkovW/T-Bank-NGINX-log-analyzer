package backend.academy.log.analyzer.service.render.error.chain;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.impl.DefaultErrorRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.error.impl.TxtErrorRenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class DefaultErrorRenderHandlerChainImplTest {

    @Mock
    private ErrorRequest mockRequest;

    @InjectMocks
    private DefaultErrorRenderHandlerChainImpl handler;

    @Test
    void shouldReturnTxtErrorRenderImplWhenTypeMatch() {
        ErrorRender result = handler.handle(mockRequest);

        assertInstanceOf(TxtErrorRenderImpl.class, result);
    }
}
