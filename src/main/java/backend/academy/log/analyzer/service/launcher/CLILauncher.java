package backend.academy.log.analyzer.service.launcher;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.cli.parser.CLIParserImpl;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.chain.factory.impl.RanderHandlerChainFactoryImpl;
import java.util.Optional;

public class CLILauncher {
    private final CLIParserImpl parser = new CLIParserImpl();

    public void launch(String[] args) {
        CLIArguments arguments = parser.parseArguments(args);

        try {
            LogAnalyzerImpl logAnalyzer = new LogAnalyzerImpl(new LogParserImpl());

            String path = arguments.path();
            String metric = arguments.filterField();
            String val = arguments.filterValue();
            Report report = logAnalyzer.generateReport(path, metric, val);

            String format = arguments.format();
            Optional<ReportRander> randerO =
                new RanderHandlerChainFactoryImpl().create().handle(new RanderRequest(format));
            String formattedReport = randerO.get().renderReportAsString(report);

            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
