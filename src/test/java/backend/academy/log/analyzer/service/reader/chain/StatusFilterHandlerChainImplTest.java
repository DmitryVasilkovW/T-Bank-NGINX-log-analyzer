package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.service.reader.chain.impl.StatusFilterHandlerChainImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusFilterHandlerChainImplTest {

    @InjectMocks
    private StatusFilterHandlerChainImpl handler;

    @Mock
    private FilterRequest mockRequest;

    @BeforeEach
    void setUp() {
        reset(mockRequest); // Сброс моков перед каждым тестом
    }

    @Test
    void shouldReturnTrueWhenStatusMatches() {
        // Arrange
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("status", "200")));
        when(mockRequest.logRecord().statusCode()).thenReturn(200);

        // Act
        boolean result = handler.handle(mockRequest);

        // Assert
        assertTrue(result);
        verify(mockRequest, times(1)).filtration();
        verify(mockRequest.logRecord(), times(1)).statusCode();
    }

    @Test
    void shouldReturnFalseWhenStatusDoesNotMatch() {
        // Arrange
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("status", "404")));
        when(mockRequest.logRecord().statusCode()).thenReturn(200);

        // Act
        boolean result = handler.handle(mockRequest);

        // Assert
        assertFalse(result);
        verify(mockRequest, times(1)).filtration();
        verify(mockRequest.logRecord(), times(1)).statusCode();
    }

    @Test
    void shouldReturnFalseWhenFiltrationValueIsInvalid() {
        // Arrange
        when(mockRequest.filtration()).thenReturn(Optional.of(new Pair<>("status", "invalid")));
        when(mockRequest.logRecord().statusCode()).thenReturn(200);

        // Act
        boolean result = handler.handle(mockRequest);

        // Assert
        assertFalse(result);
        verify(mockRequest, times(1)).filtration();
        verifyNoMoreInteractions(mockRequest.logRecord());
    }

    @Test
    void shouldThrowExceptionWhenFiltrationIsEmpty() {
        // Arrange
        when(mockRequest.filtration()).thenReturn(Optional.empty());

        // Assert
        assertThrows(RuntimeException.class, () -> {
            // Act
            handler.handle(mockRequest);
        });
        verify(mockRequest, times(1)).filtration();
        verifyNoMoreInteractions(mockRequest);
    }
}
