package backend.academy;

import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.impl.ReportRanderImpl;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            LogAnalyzerImpl logAnalyzer = new LogAnalyzerImpl();


            String path = "src/main/resources/file.log";
            logAnalyzer.readLogs(path);

            Report report = logAnalyzer.generateReport();

            String format = "adoc";
            String formattedReport = ReportRanderImpl.formatReport(report, format);

            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
