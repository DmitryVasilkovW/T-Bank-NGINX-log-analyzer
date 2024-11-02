package backend.academy;

import backend.academy.log.analyzer.LogAnalyzer;
import backend.academy.log.analyzer.Report;
import backend.academy.log.analyzer.ReportFormatter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            // Initialize LogAnalyzer
            LogAnalyzer logAnalyzer = new LogAnalyzer();

            // Set the date range (optional)
            logAnalyzer.setFrom(LocalDateTime.of(2023, 1, 1, 0, 0));
            logAnalyzer.setTo(LocalDateTime.of(2023, 12, 31, 23, 59));

            // Path to log file or URL
            String path = "src/main/resources/file.log,"; // or "http://example.com/logfile.log"
            logAnalyzer.readLogs(path);

            // Generate a report
            Report report = logAnalyzer.generateReport();

            // Choose report format: "markdown" or "adoc"
            String format = "markdown";
            String formattedReport = ReportFormatter.formatReport(report, format);

            // Print the formatted report
            System.out.println(formattedReport);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
