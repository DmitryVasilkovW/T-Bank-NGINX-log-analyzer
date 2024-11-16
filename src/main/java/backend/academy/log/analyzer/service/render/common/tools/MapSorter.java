package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapSorter {

    public static <F, S extends Number & Comparable<S>> List<Pair<F, S>> getSortedPairs(Map<F, S> map) {
        return map.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
            .sorted((p1, p2) -> p2.second().compareTo(p1.second()))
            .toList();
    }
}
