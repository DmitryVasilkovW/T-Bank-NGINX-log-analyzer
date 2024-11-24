package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.service.reader.chain.impl.AgentFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.FilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.IpFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.MethodFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.ResourceFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.StatusFilterHandlerChainImpl;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CommonFilterHandlerChainImplParameterizedTest {

    @Mock
    private FilterRequest mockRequest;

    static Stream<FilterHandlerChainImpl> provideHandlers() {
        return Stream.of(
            new AgentFilterHandlerChainImpl(),
            new IpFilterHandlerChainImpl(),
            new StatusFilterHandlerChainImpl(),
            new MethodFilterHandlerChainImpl(),
            new ResourceFilterHandlerChainImpl()
        );
    }

    @ParameterizedTest
    @MethodSource("provideHandlers")
    void shouldThrowExceptionWhenFiltrationIsEmpty(FilterHandlerChainImpl handler) {
        mockRequest = mock(FilterRequest.class);
        when(mockRequest.filtration()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> handler.handle(mockRequest));

        verify(mockRequest, times(1)).filtration();
        verifyNoMoreInteractions(mockRequest);
    }
}
