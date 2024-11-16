package backend.academy.log.analyzer.service.render.error;

import backend.academy.log.analyzer.service.render.error.impl.AdocErrorRenderImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdocErrorRenderImplTest {
    private final AdocErrorRenderImpl errorRender = new AdocErrorRenderImpl();

    @Test
    void testRender() {
        String message = "This is a test error message";

        String expected = "*Error:* This is a test error message";

        assertEquals(expected, errorRender.render(message), "The rendered error message should match the expected format.");
    }
}
