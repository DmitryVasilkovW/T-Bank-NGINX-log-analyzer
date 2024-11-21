package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import backend.academy.log.analyzer.model.Report;
import backend.academy.log.analyzer.service.render.http.response.decoder.HttpStatusDescription;
import java.util.List;
import lombok.experimental.UtilityClass;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.AVERAGE_RESPONSE_SIZE_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.DIMENSIONALITY;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.END_TIME_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.END_TIME_NO;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.FILTRATION_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.NUMBER_OF_REQUESTS_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.PATH_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.P_RESPONSE_SIZE_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.SOURCES_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.START_TIME_NAME;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.START_TIME_NO;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.UNKNOWN_CODE;
import static backend.academy.log.analyzer.service.render.common.tools.MapSorter.getSortedPairs;
import static backend.academy.log.analyzer.service.render.common.tools.StringMapper.getDataOrNoDataMessageAsString;
import static backend.academy.log.analyzer.service.render.common.tools.StringMapper.getFiltrationAsString;
import static backend.academy.log.analyzer.service.render.common.tools.StringMapper.getSourcesAsString;

@UtilityClass
public final class RenderTables {
    public static void renderSettingsTable(
        StringBuilder sb,
        Report report,
        String title,
        String header,
        String separator,
        String format
    ) {
        sb.append(title);
        sb.append(header);
        sb.append(separator);

        String path = report.settingsReport().path();
        String sources = getSourcesAsString(report.settingsReport().sources());
        String dataFrom = getDataOrNoDataMessageAsString(report.settingsReport().dateFrom(), START_TIME_NO);
        String dataTo = getDataOrNoDataMessageAsString(report.settingsReport().dateTo(), END_TIME_NO);
        String filtration = getFiltrationAsString(report.settingsReport().filtration());

        sb.append(String.format(format, PATH_NAME, path));
        sb.append(String.format(format, SOURCES_NAME, sources));
        sb.append(String.format(format, START_TIME_NAME, dataFrom));
        sb.append(String.format(format, END_TIME_NAME, dataTo));
        sb.append(String.format(format, FILTRATION_NAME, filtration));
    }

    public static void renderGeneralInfoTable(
        StringBuilder sb,
        Report report,
        String title,
        String header,
        String separator,
        String format
    ) {
        sb.append(title);
        sb.append(header);
        sb.append(separator);
        sb.append(String.format(
            format, NUMBER_OF_REQUESTS_NAME, report.totalRequests()));
        sb.append(String.format(
            format, AVERAGE_RESPONSE_SIZE_NAME, report.averageResponseSize()
                + DIMENSIONALITY));
        sb.append(String.format(
            format, P_RESPONSE_SIZE_NAME, report.percentile95ResponseSize()
                + DIMENSIONALITY));
    }

    public static void renderResourcesTable(
        StringBuilder sb,
        Report report,
        String title,
        String header,
        String separator,
        String format
    ) {
        sb.append(title);
        sb.append(header);
        sb.append(separator);

        List<Pair<String, Long>> resources = getSortedPairs(report.resourceCount());

        for (Pair<String, Long> entry : resources) {
            sb.append(String.format(format, entry.first(), entry.second()));
        }
    }

    public static void renderRequestsCodeTable(
        StringBuilder sb,
        Report report,
        String title,
        String header,
        String separator,
        String format
    ) {
        sb.append(title);
        sb.append(header);
        sb.append(separator);

        List<Pair<Integer, Long>> responseCodes = getSortedPairs(report.statusCount());

        for (Pair<Integer, Long> entry : responseCodes) {
            int code = entry.first();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append(String.format(format, code,
                description != null ? description : UNKNOWN_CODE, entry.second()));
        }
    }

    public static void renderTopResponseCodesTable(
        StringBuilder sb,
        Report report,
        String title,
        String header,
        String separator,
        String format,
        int amountForTop
    ) {
        sb.append(title);
        sb.append(header);
        sb.append(separator);

        List<Pair<Integer, Long>> responseCodes = getSortedPairs(report.statusCount());

        int n = Math.min(amountForTop, responseCodes.size());
        for (int i = 0; i < n; i++) {
            Pair<Integer, Long> entry = responseCodes.get(i);
            int code = entry.first();
            String description = HttpStatusDescription.getStatusDescription(code);
            sb.append(String.format(format, code,
                description != null ? description : UNKNOWN_CODE, entry.second()));
        }
    }
}
