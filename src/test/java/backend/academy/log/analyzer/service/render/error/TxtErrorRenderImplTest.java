package backend.academy.log.analyzer.service.render.error;

import backend.academy.log.analyzer.service.render.error.impl.TxtErrorRenderImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TxtErrorRenderImplTest {
    private final TxtErrorRenderImpl errorRender = new TxtErrorRenderImpl();

    @Test
    void testRender() {
        String message = "This is a test error message";
        String expected = "This is a test error message";

        assertEquals(expected, errorRender.render(message), "The rendered error message should be the same as the input message.");
    }
}
