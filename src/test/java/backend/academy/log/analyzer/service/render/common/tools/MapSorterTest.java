package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapSorterTest {

    @Test
    void testGetSortedPairs() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 5);
        map.put("b", 10);
        map.put("c", 2);

        List<Pair<String, Integer>> expected = List.of(
            new Pair<>("b", 10),
            new Pair<>("a", 5),
            new Pair<>("c", 2)
        );

        List<Pair<String, Integer>> result = MapSorter.getSortedPairs(map);

        assertEquals(expected, result, "The pairs should be sorted by values in descending order.");
    }

    @Test
    void testGetSortedPairsEmptyMap() {
        Map<String, Integer> map = new HashMap<>();

        List<Pair<String, Integer>> expected = List.of();

        List<Pair<String, Integer>> result = MapSorter.getSortedPairs(map);

        assertEquals(expected, result, "The result should be an empty list for an empty map.");
    }

    @Test
    void testGetSortedPairsSingleElement() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 5);

        List<Pair<String, Integer>> expected = List.of(new Pair<>("a", 5));

        List<Pair<String, Integer>> result = MapSorter.getSortedPairs(map);

        assertEquals(expected, result, "The result should contain the single pair.");
    }
}
