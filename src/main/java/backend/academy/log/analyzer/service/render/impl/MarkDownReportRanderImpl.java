package backend.academy.log.analyzer.service.render.impl;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.util.Map;

public class MarkDownReportRanderImpl implements ReportRander {
    private static final String SETTINGS_TITLE = "#### analyzer settings\n\n";

    @Override
    public String randerReportAsString(Report report) {
        var sb = new StringBuilder();
        sb.append(SETTINGS_TITLE);
        sb.append("|        Parameter        |   Value   |\n");
        sb.append("|:---------------------:|-------------:|\n");
        sb.append("|  start time  | ").append(report.dateFrom()).append(" |\n");
        sb.append("| End time | ").append(report.dateTo()).append(" |\n");
        sb.append("|   filtration  | ").append("no filtration").append(" |\n");

        sb.append("#### General information \n\n");
        sb.append("|        Metric        |     Value |\n");
        sb.append("|:---------------------:|-------------:|\n");
        sb.append("|  Number of requests  | ").append(report.totalRequests()).append(" |\n");
        sb.append("| Average response size | ").append(report.averageResponseSize()).append("b |\n");
        sb.append("|   95p response size  | ").append(report.percentile95ResponseSize()).append("b |\n");

        sb.append("\n#### Resources requested\n\n");
        sb.append("|     Resource      | Amount |\n");
        sb.append("|:---------------:|-----------:|\n");
        for (Map.Entry<String, Long> entry : report.resourceCount().entrySet()) {
            sb.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append(" |\n");
        }

        sb.append("\n#### Response Codes\n\n");
        sb.append("| Code | Description       | Amount |\n");
        sb.append("|:----:|-------------------|-------:|\n");

        for (Map.Entry<Integer, Long> entry : report.statusCount().entrySet()) {
            int code = entry.getKey();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append("| ").append(code).append(" | ").append(description != null ? description : "Unknown")
                .append(" | ").append(entry.getValue()).append(" |\n");
        }

        return sb.toString();
    }
}
