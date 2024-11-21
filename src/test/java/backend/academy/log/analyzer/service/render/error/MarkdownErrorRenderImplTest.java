package backend.academy.log.analyzer.service.render.error;

import backend.academy.log.analyzer.service.render.error.impl.MarkdownErrorRenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkdownErrorRenderImplTest {
    @Mock
    private MarkdownErrorRenderImpl mockErrorRender;

    @Test
    void testRenderWithMock() {
        String message = "This is a test error message";
        String expected = "**Error:** This is a test error message";

        when(mockErrorRender.render(message)).thenReturn(expected);

        String actual = mockErrorRender.render(message);
        assertEquals(expected, actual, "The rendered error message should match the expected format.");

        verify(mockErrorRender).render(message);
    }
}
