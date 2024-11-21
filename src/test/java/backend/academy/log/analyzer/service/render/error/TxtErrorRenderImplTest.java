package backend.academy.log.analyzer.service.render.error;

import backend.academy.log.analyzer.service.render.error.impl.TxtErrorRenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TxtErrorRenderImplTest {
    @Mock
    private TxtErrorRenderImpl mockErrorRender;

    @Test
    void testRenderWithMock() {
        String message = "This is a test error message";

        when(mockErrorRender.render(message)).thenReturn(message);

        String actual = mockErrorRender.render(message);
        assertEquals(message, actual, "The rendered error message should be the same as the input message.");

        verify(mockErrorRender).render(message);
    }
}
