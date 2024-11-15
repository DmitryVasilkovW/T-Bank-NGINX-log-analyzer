package backend.academy.log.analyzer.service.render.impl;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRander;
import java.util.Map;

public class AdocReportRanderImpl implements ReportRander {
    @Override
    public String renderReportAsString(Report report) {
        StringBuilder sb = new StringBuilder();

        sb.append("== Общая информация\n\n");
        sb.append("|===\n");
        sb.append("| Метрика | Значение\n");
        sb.append("| Количество запросов | ").append(report.totalRequests()).append("\n");
        sb.append("| Средний размер ответа | ").append(report.averageResponseSize()).append("b\n");
        sb.append("| 95p размера ответа | ").append(report.percentile95ResponseSize()).append("b\n");
        sb.append("|===\n");

        sb.append("\n== Запрашиваемые ресурсы\n\n");
        sb.append("|===\n");
        sb.append("| Ресурс | Количество\n");
        for (Map.Entry<String, Long> entry : report.resourceCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append("\n");
        }
        sb.append("|===\n");

        sb.append("\n== Коды ответа\n\n");
        sb.append("|===\n");
        sb.append("| Код | Количество\n");
        for (Map.Entry<Integer, Long> entry : report.statusCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append("\n");
        }
        sb.append("|===\n");

        return sb.toString();
    }
}
