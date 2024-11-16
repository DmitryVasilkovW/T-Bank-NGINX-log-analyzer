package backend.academy.log.analyzer.service.render.error;

import backend.academy.log.analyzer.service.render.error.impl.MarkdownErrorRenderImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkdownErrorRenderImplTest {
    private final MarkdownErrorRenderImpl errorRender = new MarkdownErrorRenderImpl();

    @Test
    void testRender() {
        String message = "This is a test error message";

        String expected = "**Error:** This is a test error message";

        assertEquals(expected, errorRender.render(message),
            "The rendered error message should match the expected format.");
    }
}
