package backend.academy.log.analyzer.service.render.error.chain;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.impl.AdocErrorRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.error.impl.AdocErrorRenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdocErrorRenderHandlerChainImplTest {

    @Mock
    private ErrorRequest mockRequest;

    @InjectMocks
    private AdocErrorRenderHandlerChainImpl handler;

    @Test
    void shouldReturnAdocErrorRenderImplWhenTypeMatch() {
        when(mockRequest.format()).thenReturn("adoc");

        ErrorRender result = handler.handle(mockRequest);

        assertInstanceOf(AdocErrorRenderImpl.class, result);
    }
}
