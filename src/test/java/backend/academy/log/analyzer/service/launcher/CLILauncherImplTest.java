package backend.academy.log.analyzer.service.launcher;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import backend.academy.log.analyzer.service.cli.printer.Printer;
import backend.academy.log.analyzer.service.launcher.impl.CLILauncherImpl;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.RenderHandlerChain;
import backend.academy.log.analyzer.service.render.chain.factory.RenderHandlerChainFactory;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.ErrorRenderHandlerChain;
import backend.academy.log.analyzer.service.render.error.chain.factory.ErrorRenderHandlerChainFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CLILauncherImplTest {

    @Mock
    private LogAnalyzer logAnalyzerMock;

    @Mock
    private CLIParser parserMock;

    @Mock
    private Printer printerMock;

    @Mock
    private RenderHandlerChain chainMock;

    @Mock
    private ErrorRenderHandlerChainFactory errorRenderHandlerChainFactoryMock;

    @Mock
    private RenderHandlerChainFactory renderHandlerChainFactoryMock;

    @Mock
    private ReportRender reportRenderMock;

    @InjectMocks
    private CLILauncherImpl cliLauncherMock;

    @Test
    void launchWithCorrectFormat() throws Exception {
        String[] args = {"--path",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--filter-field",
            "method",
            "--filter-value",
            "GET",
            "--from",
            "2023-08-31",
            "--to 2024-08-31",
            "--format markdown"
        };

        CLIArguments argumentsMock = mock(CLIArguments.class);
        Report reportMock = mock(Report.class);
        when(parserMock.parseArguments(args)).thenReturn(argumentsMock);
        when(argumentsMock.format()).thenReturn("markdown");
        when(argumentsMock.path()).thenReturn("https://raw.githubusercontent.com/elastic/examples/master/" +
            "Common%20Data%20Formats/nginx_logs/nginx_logs");
        when(argumentsMock.filterField()).thenReturn("method");
        when(argumentsMock.filterValue()).thenReturn("GET");
        when(argumentsMock.dataFrom()).thenReturn("2023-08-31");
        when(argumentsMock.dataTo()).thenReturn("2024-08-31");

        when(logAnalyzerMock.generateReport("https://raw.githubusercontent.com/elastic/examples/master/" +
            "Common%20Data%20Formats/nginx_logs/nginx_logs", "method", "GET"))
            .thenReturn(reportMock);

        when(renderHandlerChainFactoryMock.create()).thenReturn(chainMock);
        when(chainMock.handle(any())).thenReturn(Optional.of(reportRenderMock));

        cliLauncherMock.launch(args);

        verify(printerMock).println(any());
        assertDoesNotThrow(() -> cliLauncherMock.launch(args));
    }

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

        cliLauncherMock.launch(args);

        verify(printerMock).println("Error: invalid format");
    }
}
