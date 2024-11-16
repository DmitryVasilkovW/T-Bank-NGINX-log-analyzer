package backend.academy.log.analyzer.service.launcher.impl;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import backend.academy.log.analyzer.service.launcher.CLILauncher;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.factory.impl.RenderHandlerChainFactoryImpl;
import java.util.Optional;

public class CLILauncherImpl implements CLILauncher {
    private final CLIParser parser;
    private final LogAnalyzer logAnalyzer;

    public CLILauncherImpl(CLIParser parser, LogAnalyzer logAnalyzer) {
        this.parser = parser;
        this.logAnalyzer = logAnalyzer;
    }

    @Override
    public void launch(String[] args) {
        CLIArguments arguments = parser.parseArguments(args);

        try {
            String path = arguments.path();
            String metric = arguments.filterField();
            String val = arguments.filterValue();
            Report report = logAnalyzer.generateReport(path, metric, val);

            String format = arguments.format();
            Optional<ReportRender> randerO =
                new RenderHandlerChainFactoryImpl().create().handle(new RanderRequest(format));
            String formattedReport = randerO.get().renderReportAsString(report);

            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
