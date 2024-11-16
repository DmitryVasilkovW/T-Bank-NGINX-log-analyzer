package backend.academy.log.analyzer.service.launcher;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import backend.academy.log.analyzer.service.cli.printer.Printer;
import backend.academy.log.analyzer.service.launcher.impl.CLILauncherImpl;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.ErrorRenderHandlerChain;
import backend.academy.log.analyzer.service.render.error.chain.factory.ErrorRenderHandlerChainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CLILauncherImplTest {

    @Mock
    private CLIParser parserMock;

    @Mock
    private Printer printerMock;

    @Mock
    private ErrorRenderHandlerChainFactory errorRenderHandlerChainFactoryMock;

    @InjectMocks
    private CLILauncherImpl cliLauncher;

    @Test
    void launchWithInvalidFormatShouldPrintError() {
        String[] args = {"--path",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--filter-field",
            "method",
            "--filter-value",
            "GET",
            "--from",
            "2023-08-31",
            "--to 2024-08-31",
            "--format markdwn"
        };

        CLIArguments argumentsMock = mock(CLIArguments.class);
        when(parserMock.parseArguments(args)).thenReturn(argumentsMock);
        when(argumentsMock.format()).thenReturn("invalid");

        var errorRenderHandlerChainMock = mock(ErrorRenderHandlerChain.class);
        var errorRenderMock = mock(ErrorRender.class);
        when(errorRenderHandlerChainFactoryMock.create()).thenReturn(errorRenderHandlerChainMock);
        when(errorRenderHandlerChainMock.handle(any(ErrorRequest.class))).thenReturn(errorRenderMock);
        when(errorRenderMock.render(any())).thenReturn("Error: invalid format");

        cliLauncher.launch(args);

        verify(printerMock).println("Error: invalid format");
    }

}
