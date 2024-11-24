package backend.academy.log.analyzer.service.launcher.impl;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import backend.academy.log.analyzer.service.cli.printer.Printer;
import backend.academy.log.analyzer.service.launcher.CLILauncher;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.factory.RenderHandlerChainFactory;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.factory.ErrorRenderHandlerChainFactory;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Consumer;

public class CLILauncherImpl implements CLILauncher {
    private final CLIParser parser;
    private final LogAnalyzer logAnalyzer;
    private final Printer printer;
    private final ErrorRenderHandlerChainFactory errorRenderHandlerChainFactory;
    private final RenderHandlerChainFactory renderHandlerChainFactory;
    private static final String RENDER_ERROR_MESSAGE = "Rendering failed: invalid format";

    public CLILauncherImpl(
        CLIParser parser, LogAnalyzer logAnalyzer, Printer printer,
        ErrorRenderHandlerChainFactory errorRenderHandlerChainFactory,
        RenderHandlerChainFactory renderHandlerChainFactory
    ) {
        this.parser = parser;
        this.logAnalyzer = logAnalyzer;
        this.printer = printer;
        this.errorRenderHandlerChainFactory = errorRenderHandlerChainFactory;
        this.renderHandlerChainFactory = renderHandlerChainFactory;
    }

    @Override
    public void launch(String[] args) {
        String format;
        CLIArguments arguments = parser.parseArguments(args);
        format = arguments.format();

        try {
            analyze(arguments, format);
        } catch (Exception e) {
            ErrorRender render = errorRenderHandlerChainFactory
                .create()
                .handle(new ErrorRequest(format));

            printer.println(render.render(e.getMessage()));
        }
    }

    private void analyze(CLIArguments arguments, String format) throws Exception {
        String path = arguments.path();
        String metric = arguments.filterField();
        String val = arguments.filterValue();
        setData(arguments.dataFrom(), logAnalyzer::setTimeFrom);
        setData(arguments.dataTo(), logAnalyzer::setTimeTo);

        Report report = logAnalyzer.generateReport(path, metric, val);

        Optional<ReportRender> randerO =
            renderHandlerChainFactory.create().handle(new RenderRequest(format));
        String formattedReport = randerO
            .orElseThrow(() -> new RuntimeException(RENDER_ERROR_MESSAGE)).renderReportAsString(report);

        printer.println(formattedReport);
    }

    private void setData(String data, Consumer<OffsetDateTime> setter) {
        if (!data.isEmpty()) {
            setter.accept(
                LocalDate.parse(data).atStartOfDay().atOffset(ZoneOffset.UTC)
            );
        }
    }
}
