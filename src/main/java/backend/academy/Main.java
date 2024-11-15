package backend.academy;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import backend.academy.log.analyzer.service.render.impl.ReportRanderImpl;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            LogAnalyzerImpl logAnalyzer = new LogAnalyzerImpl(new LogParserImpl());

            String path = "src/main/resources/file.log";
            logAnalyzer.readLogs(path);

            Report report = logAnalyzer.generateReport();

            String format = "markdown";
            String formattedReport = ReportRanderImpl.formatReport(report, format);

            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
