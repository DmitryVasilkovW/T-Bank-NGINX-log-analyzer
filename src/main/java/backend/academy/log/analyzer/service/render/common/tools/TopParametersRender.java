package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TopParametersRender {

    public static void renderTopParameterTable(
        StringBuilder sb,
        List<Pair<String, Long>> parameters,
        int amountForTop,
        String format
    ) {
        int n = Math.min(amountForTop, parameters.size());
        for (int i = 0; i < n; i++) {
            Pair<String, Long> entry = parameters.get(i);
            sb.append(String.format(format, entry.first(), entry.second()));
        }
    }
}
