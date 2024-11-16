package backend.academy.log.analyzer.service.render.impl;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class AdocReportRenderImpl implements ReportRender {
    StringBuilder sb;
    List<Pair<Integer, Long>> responseCodes;
    List<Pair<String, Long>> resources;
    private static final Integer AMOUNT_FOR_TOP = 3;
    private static final String GENERAL_TITLE = "== All information\n";
    private static final String COMPILATIONS_TITLE = "== Compilations\n";
    private static final String SETTINGS_TITLE = "=== analyzer settings\n\n";
    private static final String GENERAL_INFO_TITLE = "=== General information\n\n";
    private static final String RESOURCES_REQUESTED_TITLE = "\n=== Resources requested\n\n";
    private static final String RESPONSE_CODES_TITLE = "\n=== Response Codes\n\n";
    private static final String TOP_OF_RESOURCES_TITLE = "=== three most requested resources\n\n";
    private static final String TOP_OF_RESPONSE_CODES_TITLE = "=== three most response codes\n\n";
    private static final String TOP_OF_IP_ADDRESSES_TITLE = "=== three most active IP addresses\n\n";
    private static final String TOP_OF_USER_AGENTS_TITLE = "=== three most requested user agents\n\n";

    private static final String SETTINGS_TABLE_HEADER = "| Parameter | Value\n";
    private static final String IP_ADDRESSES_TABLE_HEADER = "| IP address | Amount\n";
    private static final String USER_AGENTS_TABLE_HEADER = "| User agent | Amount\n";
    private static final String GENERAL_INFO_TABLE_HEADER = "| Metric | Value\n";
    private static final String RESOURCES_TABLE_HEADER = "| Resource | Amount\n";
    private static final String RESPONSE_CODES_TABLE_HEADER = "| Code | Description | Amount\n";

    private static final String TABLE_SEPARATOR = "|===\n";

    private static final String FORMAT_FOR_TWO_STRINGS = "| %s | %s\n";
    private static final String FORMAT_FOR_STRING_AND_INTEGER = "| %s | %d\n";
    private static final String FORMAT_FOR_TWOINTEGERS_AND_ONE_STRING = "| %d | %s | %d\n";
    private static final String FORMAT_FOR_FILTRATION = "in %s metric to show only %s";

    private static final String FILTRATION_NO = "No filtration";
    private static final String START_TIME_NAME = "Start time";
    private static final String END_TIME_NAME = "End time";
    private static final String START_TIME_NO = "No start time";
    private static final String END_TIME_NO = "No end time";
    private static final String FILTRATION_NAME = "Filtration";
    private static final String NUMBER_OF_REQUESTS_NAME = "Number of requests";
    private static final String AVERAGE_RESPONSE_SIZE_NAME = "Average response size";
    private static final String P_RESPONSE_SIZE_NAME = "95p response size";
    private static final String DIMENSIONALITY = "b";
    private static final String UNKNOWN_CODE = "Unknown code";
    private static final String SOURCES_NAME = "Sources";
    private static final String PATH_NAME = "Path";

    @Override
    public String renderReportAsString(Report report) {
        sb = new StringBuilder();
        renderGeneralInformation(report);
        renderCompilations(report);

        return sb.toString();
    }

    private void renderGeneralInformation(Report report) {
        sb.append(GENERAL_TITLE);
        renderSettingsTable(report);
        renderGeneralInfoTable(report);
        renderResourcesTable(report);
        renderRequestsCodeTable(report);
    }

    private void renderCompilations(Report report) {
        sb.append(COMPILATIONS_TITLE);
        renderTopRequestedResourcesTable(report);
        renderTopResponseCodesTable(report);
        renderTopIpAddressesTable(report);
        renderTopUserAgentsTable(report);
    }

    private void renderSettingsTable(Report report) {
        sb.append(SETTINGS_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(SETTINGS_TABLE_HEADER);

        String path = report.settingsReport().path();
        String sources = getSources(report.settingsReport().sources());
        String dataFrom = getDataAsString(report.settingsReport().dateFrom(), START_TIME_NO);
        String dataTo = getDataAsString(report.settingsReport().dateTo(), END_TIME_NO);
        String filtration = getFiltrationAsString(report.settingsReport().filtration());

        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, PATH_NAME, path));
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, SOURCES_NAME, sources));
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, START_TIME_NAME, dataFrom));
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, END_TIME_NAME, dataTo));
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, FILTRATION_NAME, filtration));
        sb.append(TABLE_SEPARATOR);
    }

    private String getFiltrationAsString(Pair<String, String> filtration) {
        if (filtration == null) {
            return FILTRATION_NO;
        }

        return String.format(FORMAT_FOR_FILTRATION, filtration.first(), filtration.second());
    }

    private String getDataAsString(OffsetDateTime date, String message) {
        if (date == null) {
            return message;
        }

        return date.toString();
    }

    private String getSources(List<String> sources) {
        return String.join(", ", sources);
    }

    private void renderGeneralInfoTable(Report report) {
        sb.append(GENERAL_INFO_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(GENERAL_INFO_TABLE_HEADER);
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, NUMBER_OF_REQUESTS_NAME, report.totalRequests()));
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, AVERAGE_RESPONSE_SIZE_NAME,
            report.averageResponseSize() + DIMENSIONALITY));
        sb.append(String.format(FORMAT_FOR_TWO_STRINGS, P_RESPONSE_SIZE_NAME,
            report.percentile95ResponseSize() + DIMENSIONALITY));
        sb.append(TABLE_SEPARATOR);
    }

    private void renderResourcesTable(Report report) {
        sb.append(RESOURCES_REQUESTED_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(RESOURCES_TABLE_HEADER);

        if (resources == null) {
            resources = getSortedPairs(report.resourceCount());
        }

        for (Pair<String, Long> entry : resources) {
            sb.append(String.format(FORMAT_FOR_STRING_AND_INTEGER, entry.first(), entry.second()));
        }
        sb.append(TABLE_SEPARATOR);
    }

    private void renderRequestsCodeTable(Report report) {
        sb.append(RESPONSE_CODES_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(RESPONSE_CODES_TABLE_HEADER);

        if (responseCodes == null) {
            responseCodes = getSortedPairs(report.statusCount());
        }

        for (Pair<Integer, Long> entry : responseCodes) {
            int code = entry.first();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append(String.format(FORMAT_FOR_TWOINTEGERS_AND_ONE_STRING, code,
                description != null ? description : UNKNOWN_CODE, entry.second()));
        }
        sb.append(TABLE_SEPARATOR);
    }

    private void renderTopRequestedResourcesTable(Report report) {
        sb.append(TOP_OF_RESOURCES_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(RESOURCES_TABLE_HEADER);

        if (resources == null) {
            resources = getSortedPairs(report.resourceCount());
        }

        renderTopParameterTable(resources);
        sb.append(TABLE_SEPARATOR);
    }

    private void renderTopResponseCodesTable(Report report) {
        sb.append(TOP_OF_RESPONSE_CODES_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(RESPONSE_CODES_TABLE_HEADER);

        if (responseCodes == null) {
            responseCodes = getSortedPairs(report.statusCount());
        }

        int n = Math.min(AMOUNT_FOR_TOP, responseCodes.size());
        for (int i = 0; i < n; i++) {
            Pair<Integer, Long> entry = responseCodes.get(i);
            int code = entry.first();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append(String.format(FORMAT_FOR_TWOINTEGERS_AND_ONE_STRING, code,
                description != null ? description : UNKNOWN_CODE, entry.second()));
        }
        sb.append(TABLE_SEPARATOR);
    }

    private void renderTopIpAddressesTable(Report report) {
        sb.append(TOP_OF_IP_ADDRESSES_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(IP_ADDRESSES_TABLE_HEADER);

        List<Pair<String, Long>> addresses = getSortedPairs(report.ipAddresses());
        renderTopParameterTable(addresses);
        sb.append(TABLE_SEPARATOR);
    }

    private void renderTopUserAgentsTable(Report report) {
        sb.append(TOP_OF_USER_AGENTS_TITLE);
        sb.append(TABLE_SEPARATOR);
        sb.append(USER_AGENTS_TABLE_HEADER);

        List<Pair<String, Long>> userAgents = getSortedPairs(report.userAgents());
        renderTopParameterTable(userAgents);
        sb.append(TABLE_SEPARATOR);
    }

    private void renderTopParameterTable(List<Pair<String, Long>> parameters) {
        int n = Math.min(AMOUNT_FOR_TOP, parameters.size());
        for (int i = 0; i < n; i++) {
            Pair<String, Long> entry = parameters.get(i);
            sb.append(String.format(FORMAT_FOR_STRING_AND_INTEGER, entry.first(), entry.second()));
        }
    }

    private <F, S extends Number & Comparable<S>> List<Pair<F, S>> getSortedPairs(Map<F, S> map) {
        return map.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
            .sorted((p1, p2) -> p2.second().compareTo(p1.second()))
            .toList();
    }
}
