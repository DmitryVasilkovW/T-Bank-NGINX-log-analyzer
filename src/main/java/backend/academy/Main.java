package backend.academy;

import backend.academy.log.analyzer.LogAnalyzer;
import backend.academy.log.analyzer.Report;
import backend.academy.log.analyzer.ReportFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            LogAnalyzer logAnalyzer = new LogAnalyzer();


            String path = "src/main/resources/file.log";
            logAnalyzer.readLogs(path);

            Report report = logAnalyzer.generateReport();

            String format = "adoc";
            String formattedReport = ReportFormatter.formatReport(report, format);

            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
