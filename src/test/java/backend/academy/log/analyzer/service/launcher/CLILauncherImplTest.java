package backend.academy.log.analyzer.service.launcher;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import backend.academy.log.analyzer.service.cli.printer.Printer;
import backend.academy.log.analyzer.service.launcher.impl.CLILauncherImpl;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.ErrorRenderHandlerChain;
import backend.academy.log.analyzer.service.render.error.chain.factory.ErrorRenderHandlerChainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CLILauncherImplTest {

    @Mock
    private CLIParser parserMock;

    @Mock
    private LogAnalyzer logAnalyzerMock;

    @Mock
    private Printer printerMock;

    @Mock
    private ErrorRenderHandlerChainFactory errorRenderHandlerChainFactoryMock;

    @InjectMocks
    private CLILauncherImpl cliLauncher;

    @Test
    void launch_withInvalidFormat_shouldPrintError() {
        // Arrange
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

        // Создаем мок для ErrorRenderHandlerChain
        var errorRenderHandlerChainMock = mock(ErrorRenderHandlerChain.class);
        var errorRenderMock = mock(ErrorRender.class);

        // Настройка возвращаемых значений
        when(errorRenderHandlerChainFactoryMock.create()).thenReturn(errorRenderHandlerChainMock);
        when(errorRenderHandlerChainMock.handle(any(ErrorRequest.class))).thenReturn(errorRenderMock);
        when(errorRenderMock.render(anyString())).thenReturn("Error: invalid format");

        // Act
        cliLauncher.launch(args);

        // Assert
        verify(printerMock).println("Error: invalid format");
    }

    @Test
    void launch_validArguments_withMocks_shouldPrintFormattedReport() throws Exception {
        // Arrange
        String[] args = {
            "--path",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--filter-field",
            "method",
            "--filter-value",
            "GET",
            "--from",
            "2023-08-31",
            "--to",
            "2024-08-31",
            "--format",
            "md"
        };

        var cliArgumentsMock = mock(CLIArguments.class);
        when(cliArgumentsMock.format()).thenReturn("md");
        when(parserMock.parseArguments(args)).thenReturn(cliArgumentsMock);

        var errorRenderHandlerChainMock = mock(ErrorRenderHandlerChain.class);
        var errorRenderMock = mock(ErrorRender.class);
        when(errorRenderHandlerChainFactoryMock.create()).thenReturn(errorRenderHandlerChainMock);
        when(errorRenderHandlerChainMock.handle(any(ErrorRequest.class))).thenReturn(errorRenderMock);

        Report reportMock = mock(Report.class);
        when(logAnalyzerMock.generateReport("log.txt", "method", "GET")).thenReturn(reportMock);

        ReportRender reportRenderMock = mock(ReportRender.class);
        when(reportRenderMock.renderReportAsString(reportMock)).thenReturn("{\"report\":\"success\"}");

        // Act
        cliLauncher.launch(args);

        // Assert
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(printerMock).println(captor.capture());

        verify(parserMock).parseArguments(args);
        verify(reportRenderMock).renderReportAsString(reportMock);
    }


}
