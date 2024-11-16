package backend.academy.log.analyzer.service.render.impl;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRender;
import java.util.List;
import static backend.academy.log.analyzer.service.render.common.tools.MapSorter.getSortedPairs;
import static backend.academy.log.analyzer.service.render.common.tools.RenderTables.renderGeneralInfoTable;
import static backend.academy.log.analyzer.service.render.common.tools.RenderTables.renderRequestsCodeTable;
import static backend.academy.log.analyzer.service.render.common.tools.RenderTables.renderResourcesTable;
import static backend.academy.log.analyzer.service.render.common.tools.RenderTables.renderSettingsTable;
import static backend.academy.log.analyzer.service.render.common.tools.RenderTables.renderTopResponseCodesTable;
import static backend.academy.log.analyzer.service.render.common.tools.TopParametersRender.renderTopParameterTable;

public class MarkdownReportRenderImpl implements ReportRender {
    StringBuilder sb;
    List<Pair<String, Long>> resources;
    private static final Integer AMOUNT_FOR_TOP = 3;
    private static final String GENERAL_TITLE = "# All information\n";
    private static final String COMPILATIONS_TITLE = "# Compilations\n";
    private static final String SETTINGS_TITLE = "### analyzer settings\n\n";
    private static final String GENERAL_INFO_TITLE = "### General information \n\n";
    private static final String RESOURCES_REQUESTED_TITLE = "\n### Resources requested\n\n";
    private static final String RESPONSE_CODES_TITLE = "\n### Response Codes\n\n";
    private static final String TOP_OF_RESOURCES_TITLE = "### three most requested resources\n\n";
    private static final String TOP_OF_RESPONSE_CODES_TITLE = "### three most response codes\n\n";
    private static final String TOP_OF_IP_ADDRESSES_TITLE = "### three most active IP addresses\n\n";
    private static final String TOP_OF_USER_AGENTS_TITLE = "### three most requested user agents\n\n";

    private static final String SETTINGS_TABLE_HEADER = "|        Parameter        |   Value   |\n";
    private static final String IP_ADDRESSES_TABLE_HEADER = "|     IP address     |    Amount    |\n";
    private static final String USER_AGENTS_TABLE_HEADER = "|     User agent     |    Amount    |\n";
    private static final String GENERAL_INFO_TABLE_HEADER = "|        Metric        |     Value |\n";
    private static final String RESOURCES_TABLE_HEADER = "|     Resource      | Amount |\n";
    private static final String RESPONSE_CODES_TABLE_HEADER = "| Code | Description       | Amount |\n";

    private static final String BIG_TABLE_SEPARATOR =
        "|:---------------------:|-------------:|\n";
    private static final String MEDIUM_TABLE_SEPARATOR =
        "|:---------------:|-----------:|\n";
    private static final String TABLE_SEPARATOR_FOR_THREE_COLUMN =
        "|:----:|-------------------|-------:|\n";

    private static final String FORMAT_FOR_TWO_STRINGS = "|  %s  | %s |\n";
    private static final String FORMAT_FOR_STRING_AND_INTEGER = "| %s | %d |\n";
    private static final String FORMAT_FOR_TWOINTEGERS_AND_ONE_STRING = "| %d | %s | %d |\n";

    @Override
    public String renderReportAsString(Report report) {
        sb = new StringBuilder();
        renderGeneralInformation(report);
        renderCompilations(report);

        return sb.toString();
    }

    private void renderGeneralInformation(Report report) {
        sb.append(GENERAL_TITLE);
        renderSettingsTable(sb, report, SETTINGS_TITLE, SETTINGS_TABLE_HEADER, BIG_TABLE_SEPARATOR,
            FORMAT_FOR_TWO_STRINGS);
        renderGeneralInfoTable(sb, report, GENERAL_INFO_TITLE, GENERAL_INFO_TABLE_HEADER, BIG_TABLE_SEPARATOR,
            FORMAT_FOR_TWO_STRINGS);
        renderResourcesTable(sb, report, RESOURCES_REQUESTED_TITLE, RESOURCES_TABLE_HEADER, MEDIUM_TABLE_SEPARATOR,
            FORMAT_FOR_STRING_AND_INTEGER);
        renderRequestsCodeTable(sb, report, RESPONSE_CODES_TITLE, RESPONSE_CODES_TABLE_HEADER,
            TABLE_SEPARATOR_FOR_THREE_COLUMN, FORMAT_FOR_TWOINTEGERS_AND_ONE_STRING);
    }

    private void renderCompilations(Report report) {
        sb.append(COMPILATIONS_TITLE);
        renderTopRequestedResourcesTable(report);
        renderTopResponseCodesTable(sb, report, TOP_OF_RESPONSE_CODES_TITLE, RESPONSE_CODES_TABLE_HEADER,
            TABLE_SEPARATOR_FOR_THREE_COLUMN, FORMAT_FOR_TWOINTEGERS_AND_ONE_STRING, AMOUNT_FOR_TOP);
        renderTopIpAddressesTable(report);
        renderTopUserAgentsTable(report);
    }

    private void renderTopRequestedResourcesTable(Report report) {
        sb.append(TOP_OF_RESOURCES_TITLE);
        sb.append(RESOURCES_TABLE_HEADER);
        sb.append(MEDIUM_TABLE_SEPARATOR);

        if (resources == null) {
            resources = getSortedPairs(report.resourceCount());
        }

        renderTopParameterTable(sb, resources, AMOUNT_FOR_TOP, FORMAT_FOR_STRING_AND_INTEGER);
    }

    private void renderTopIpAddressesTable(Report report) {
        sb.append(TOP_OF_IP_ADDRESSES_TITLE);
        sb.append(IP_ADDRESSES_TABLE_HEADER);
        sb.append(MEDIUM_TABLE_SEPARATOR);

        List<Pair<String, Long>> addresses = getSortedPairs(report.ipAddresses());
        renderTopParameterTable(sb, addresses, AMOUNT_FOR_TOP, FORMAT_FOR_STRING_AND_INTEGER);
    }

    private void renderTopUserAgentsTable(Report report) {
        sb.append(TOP_OF_USER_AGENTS_TITLE);
        sb.append(USER_AGENTS_TABLE_HEADER);
        sb.append(MEDIUM_TABLE_SEPARATOR);

        List<Pair<String, Long>> userAgents = getSortedPairs(report.userAgents());
        renderTopParameterTable(sb, userAgents, AMOUNT_FOR_TOP, FORMAT_FOR_STRING_AND_INTEGER);
    }
}
