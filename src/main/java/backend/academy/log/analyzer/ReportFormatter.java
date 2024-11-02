package backend.academy.log.analyzer;

import java.util.Map;

public class ReportFormatter {

    public static String formatReport(Report report, String format) {
        switch (format.toLowerCase()) {
            case "markdown":
                return formatAsMarkdown(report);
            case "adoc":
                return formatAsAdoc(report);
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    private static String formatAsMarkdown(Report report) {
        StringBuilder sb = new StringBuilder();

        sb.append("#### Общая информация\n\n");
        sb.append("|        Метрика        |     Значение |\n");
        sb.append("|:---------------------:|-------------:|\n");
        sb.append("|  Количество запросов  | ").append(report.getTotalRequests()).append(" |\n");
        sb.append("| Средний размер ответа | ").append(report.getAverageResponseSize()).append("b |\n");
        sb.append("|   95p размера ответа  | ").append(report.getPercentile95ResponseSize()).append("b |\n");

        sb.append("\n#### Запрашиваемые ресурсы\n\n");
        sb.append("|     Ресурс      | Количество |\n");
        sb.append("|:---------------:|-----------:|\n");
        for (Map.Entry<String, Long> entry : report.getResourceCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append(" |\n");
        }

        sb.append("\n#### Коды ответа\n\n");
        sb.append("| Код | Количество |\n");
        sb.append("|:---:|-----------:|\n");
        for (Map.Entry<Integer, Long> entry : report.getStatusCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append(" |\n");
        }

        return sb.toString();
    }

    private static String formatAsAdoc(Report report) {
        StringBuilder sb = new StringBuilder();

        sb.append("== Общая информация\n\n");
        sb.append("|===\n");
        sb.append("| Метрика | Значение\n");
        sb.append("| Количество запросов | ").append(report.getTotalRequests()).append("\n");
        sb.append("| Средний размер ответа | ").append(report.getAverageResponseSize()).append("b\n");
        sb.append("| 95p размера ответа | ").append(report.getPercentile95ResponseSize()).append("b\n");
        sb.append("|===\n");

        sb.append("\n== Запрашиваемые ресурсы\n\n");
        sb.append("|===\n");
        sb.append("| Ресурс | Количество\n");
        for (Map.Entry<String, Long> entry : report.getResourceCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append("\n");
        }
        sb.append("|===\n");

        sb.append("\n== Коды ответа\n\n");
        sb.append("|===\n");
        sb.append("| Код | Количество\n");
        for (Map.Entry<Integer, Long> entry : report.getStatusCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append("\n");
        }
        sb.append("|===\n");

        return sb.toString();
    }
}
