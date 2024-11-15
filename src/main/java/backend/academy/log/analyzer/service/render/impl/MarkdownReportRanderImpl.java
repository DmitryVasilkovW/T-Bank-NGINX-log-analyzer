package backend.academy.log.analyzer.service.render.impl;

import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.util.List;
import java.util.Map;

public class MarkdownReportRanderImpl implements ReportRander {
    StringBuilder sb;
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

    private static final String FILTRATION_NO = "No filtration";
    private static final String START_TIME_NAME = "Start time";
    private static final String END_TIME_NAME = "End time";
    private static final String FILTRATION_NAME = "Filtration";
    private static final String NUMBER_OF_REQUESTS_NAME = "Number of requests";
    private static final String AVERAGE_RESPONSE_SIZE_NAME = "Average response size";
    private static final String P_RESPONSE_SIZE_NAME = "95p response size";
    private static final String DIMENSIONALITY = "b";
    private static final String UNKNOWN_CODE = "Unknown code";
    private static final String SOURCES_NAME = "Sources";
    private static final String PATH_NAME = "Path";

    @Override
    public String randerReportAsString(Report report) {
        sb = new StringBuilder();
        renderSettingsTable(report);
        renderGeneralInfoTable(report);
        renderResourcesTable(report);
        renderRequestsCodeTable(report);

        return sb.toString();
    }

    private void renderSettingsTable(Report report) {
        sb.append(SETTINGS_TITLE);
        sb.append(SETTINGS_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_PARAMETERS_AND_METRICS);
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, PATH_NAME, report.path()));
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, SOURCES_NAME, getSources(report.sources())));
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, START_TIME_NAME, report.dateFrom()));
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, END_TIME_NAME, report.dateTo()));
        sb.append(String.format(GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, FILTRATION_NAME, FILTRATION_NO));
    }

    private String getSources(List<String> sources) {
        return String.join(", ", sources);
    }

    private void renderGeneralInfoTable(Report report) {
        sb.append(GENERAL_INFO_TITLE);
        sb.append(GENERAL_INFO_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_PARAMETERS_AND_METRICS);
        sb.append(String.format(
            GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, NUMBER_OF_REQUESTS_NAME, report.totalRequests()));
        sb.append(String.format(
            GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, AVERAGE_RESPONSE_SIZE_NAME, report.averageResponseSize()
                + DIMENSIONALITY));
        sb.append(String.format(
            GENERAL_INFO_ROW_AND_SETTINGS_ROW_FORMAT, P_RESPONSE_SIZE_NAME, report.percentile95ResponseSize()
                + DIMENSIONALITY));
    }

    private void renderResourcesTable(Report report) {
        sb.append(RESOURCES_REQUESTED_TITLE);
        sb.append(RESOURCES_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_RESOURCES);
        for (Map.Entry<String, Long> entry : report.resourceCount().entrySet()) {
            sb.append(String.format(RESOURCES_ROW_FORMAT, entry.getKey(), entry.getValue()));
        }
    }

    private void renderRequestsCodeTable(Report report) {
        sb.append(RESPONSE_CODES_TITLE);
        sb.append(RESPONSE_CODES_TABLE_HEADER);
        sb.append(TABLE_SEPARATOR_FOR_RESPONSE_CODES);

        for (Map.Entry<Integer, Long> entry : report.statusCount().entrySet()) {
            int code = entry.getKey();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append(String.format(RESPONSE_CODES_ROW_FORMAT, code,
                description != null ? description : UNKNOWN_CODE, entry.getValue()));
        }
    }
}
