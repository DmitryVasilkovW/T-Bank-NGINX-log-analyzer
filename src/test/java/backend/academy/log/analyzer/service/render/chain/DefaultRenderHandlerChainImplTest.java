package backend.academy.log.analyzer.service.render.chain;

import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.impl.DefaultRenderHandlerChainImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DefaultRenderHandlerChainImplTest {

    @Mock
    private RenderRequest mockRequest;

    @InjectMocks
    private DefaultRenderHandlerChainImpl handler;

    @Test
    void shouldReturnNothingWhenTypeMatch() {

        Optional<ReportRender> resultO = handler.handle(mockRequest);

        assertTrue(resultO.isEmpty());
    }
}
