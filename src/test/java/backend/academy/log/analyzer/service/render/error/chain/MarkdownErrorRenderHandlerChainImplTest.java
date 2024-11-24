package backend.academy.log.analyzer.service.render.error.chain;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.impl.MarkdownErrorRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.error.impl.MarkdownErrorRenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkdownErrorRenderHandlerChainImplTest {

    @Mock
    private ErrorRequest mockRequest;

    @InjectMocks
    private MarkdownErrorRenderHandlerChainImpl handler;

    @Test
    void shouldReturnMarkdownErrorRenderImplWhenTypeMatch() {
        when(mockRequest.format()).thenReturn("markdown");

        ErrorRender result = handler.handle(mockRequest);

        assertInstanceOf(MarkdownErrorRenderImpl.class, result);
    }
}
