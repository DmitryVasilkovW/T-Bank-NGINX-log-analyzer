package backend.academy.log.analyzer.service.render.chain;

import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.impl.AdocRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.impl.AdocReportRenderImpl;
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
class AdocRenderHandlerChainImplTest {

    @Mock
    private RenderRequest mockRequest;

    @InjectMocks
    private AdocRenderHandlerChainImpl handler;

    @Test
    void shouldReturnAdocReportRenderImplWhenTypeMatch() {
        when(mockRequest.format()).thenReturn("adoc");

        Optional<ReportRender> resultO = handler.handle(mockRequest);

        assertTrue(resultO.isPresent());
        assertInstanceOf(AdocReportRenderImpl.class, resultO.get());
    }
}
