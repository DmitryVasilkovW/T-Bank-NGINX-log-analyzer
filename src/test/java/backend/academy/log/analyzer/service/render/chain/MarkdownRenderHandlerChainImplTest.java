package backend.academy.log.analyzer.service.render.chain;

import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.impl.MarkdownRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.impl.MarkdownReportRenderImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkdownRenderHandlerChainImplTest {

    @Mock
    private RenderRequest mockRequest;

    @InjectMocks
    private MarkdownRenderHandlerChainImpl handler;

    @Test
    void shouldReturnMarkdownReportRenderImplWhenTypeMatch() {
        when(mockRequest.format()).thenReturn("markdown");

        Optional<ReportRender> resultO = handler.handle(mockRequest);

        assertTrue(resultO.isPresent());
        assertInstanceOf(MarkdownReportRenderImpl.class, resultO.get());
    }
}
