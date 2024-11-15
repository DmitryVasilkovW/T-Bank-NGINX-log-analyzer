package backend.academy;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.chain.factory.impl.RanderHandlerChainFactoryImpl;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            LogAnalyzerImpl logAnalyzer = new LogAnalyzerImpl(new LogParserImpl());

            String path = "src/main/resources/logs/*.log";
            logAnalyzer.readLogs(path);

            Report report = logAnalyzer.generateReport();

            String format = "markdown";
            Optional<ReportRander> randerO =
                new RanderHandlerChainFactoryImpl().create().handle(new RanderRequest(format));
            String formattedReport = randerO.get().randerReportAsString(report);

            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
