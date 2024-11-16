package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.experimental.UtilityClass;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.FILTRATION_NO;
import static backend.academy.log.analyzer.service.render.common.constant.CommonConstant.FORMAT_FOR_FILTRATION;

@UtilityClass
public final class StringMapper {

    public static String getFiltrationAsString(Pair<String, String> filtration) {
        if (filtration == null) {
            return FILTRATION_NO;
        }

        return String.format(FORMAT_FOR_FILTRATION, filtration.first(), filtration.second());
    }

    public static String getDataAsString(OffsetDateTime date, String message) {
        if (date == null) {
            return message;
        }

        return date.toString();
    }

    public static String getSourcesAsString(List<String> sources) {
        return String.join(", ", sources);
    }
}
