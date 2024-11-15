package backend.academy.log.analyzer.service.render.impl;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.util.Map;

public class MarkdownReportRanderImpl implements ReportRander {
    private static final String SETTINGS_TITLE = "#### analyzer settings\n\n";
    private static final String GENERAL_INFO_TITLE = "#### General information \n\n";
    private static final String RESOURCES_REQUESTED_TITLE = "\n#### Resources requested\n\n";
    private static final String RESPONSE_CODES_TITLE = "\n#### Response Codes\n\n";

    private static final String SETTINGS_TABLE_HEADER = "|        Parameter        |   Value   |\n";
    private static final String GENERAL_INFO_TABLE_HEADER = "|        Metric        |     Value |\n";
    private static final String RESOURCES_TABLE_HEADER = "|     Resource      | Amount |\n";
    private static final String RESPONSE_CODES_TABLE_HEADER = "| Code | Description       | Amount |\n";

    private static final String TABLE_SEPARATOR_FOR_PARAMETERS_AND_METRICS =
        "|:---------------------:|-------------:|\n";
    private static final String TABLE_SEPARATOR_FOR_RESOURCES =
        "|:---------------:|-----------:|\n";
    private static final String TABLE_SEPARATOR_FOR_RESPONSE_CODES =
        "|:----:|-------------------|-------:|\n";

    private static final String GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT = "|  %s  | %s |\n";
    private static final String RESOURCES_ROW_FORMAT = "| %s | %d |\n";
    private static final String RESPONSE_CODES_ROW_FORMAT = "| %d | %s | %d |\n";

    private static final String FILTRATION_NO = "no filtration";

    @Override
    public String randerReportAsString(Report report) {
        var sb = new StringBuilder();
        sb.append(SETTINGS_TITLE);
        sb.append(SETTINGS_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_PARAMETERS_AND_METRICS);
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, "start time", report.dateFrom()));
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, "End time", report.dateTo()));
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, "filtration", FILTRATION_NO));

        sb.append(GENERAL_INFO_TITLE);
        sb.append(GENERAL_INFO_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_PARAMETERS_AND_METRICS);
        sb.append(String.format(
            GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, "Number of requests", report.totalRequests()));
        sb.append(String.format(
            GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, "Average response size", report.averageResponseSize() + "b"));
        sb.append(String.format(
            GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, "95p response size", report.percentile95ResponseSize() + "b"));

        sb.append(RESOURCES_REQUESTED_TITLE);
        sb.append(RESOURCES_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_RESOURCES);
        for (Map.Entry<String, Long> entry : report.resourceCount().entrySet()) {
            sb.append(String.format(RESOURCES_ROW_FORMAT, entry.getKey(), entry.getValue()));
        }

        sb.append(RESPONSE_CODES_TITLE);
        sb.append(RESPONSE_CODES_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_RESPONSE_CODES);

        for (Map.Entry<Integer, Long> entry : report.statusCount().entrySet()) {
            int code = entry.getKey();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append(String.format(RESPONSE_CODES_ROW_FORMAT, code,
                description != null ? description : "Unknown", entry.getValue()));
        }

        return sb.toString();
    }
}
