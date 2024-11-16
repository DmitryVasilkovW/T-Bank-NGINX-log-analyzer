package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopParametersRenderTest {

    @Test
    void testRenderTopParameterTable() {
        List<Pair<String, Long>> parameters = List.of(
            new Pair<>("Param1", 10L),
            new Pair<>("Param2", 20L),
            new Pair<>("Param3", 30L)
        );
        StringBuilder sb = new StringBuilder();
        String format = "Parameter: %s, Value: %d\n";

        TopParametersRender.renderTopParameterTable(sb, parameters, 2, format);

        String expected = "Parameter: Param1, Value: 10\nParameter: Param2, Value: 20\n";
        assertEquals(expected, sb.toString(), "The rendered table should correctly format the top parameters.");
    }

    @Test
    void testRenderTopParameterTableWithAmountLessThanSize() {
        List<Pair<String, Long>> parameters = List.of(
            new Pair<>("Param1", 10L),
            new Pair<>("Param2", 20L),
            new Pair<>("Param3", 30L)
        );
        StringBuilder sb = new StringBuilder();
        String format = "Parameter: %s, Value: %d\n";

        TopParametersRender.renderTopParameterTable(sb, parameters, 2, format);

        String expected = "Parameter: Param1, Value: 10\nParameter: Param2, Value: 20\n";
        assertEquals(expected, sb.toString(), "The table should render only the top `amountForTop` parameters.");
    }

    @Test
    void testRenderTopParameterTableWithAmountGreaterThanSize() {
        List<Pair<String, Long>> parameters = List.of(
            new Pair<>("Param1", 10L),
            new Pair<>("Param2", 20L)
        );
        StringBuilder sb = new StringBuilder();
        String format = "Parameter: %s, Value: %d\n";

        TopParametersRender.renderTopParameterTable(sb, parameters, 5, format);

        String expected = "Parameter: Param1, Value: 10\nParameter: Param2, Value: 20\n";
        assertEquals(expected, sb.toString(),
            "The table should render all parameters when `amountForTop` is greater than the size of the list.");
    }

    @Test
    void testRenderTopParameterTableWithEmptyList() {
        List<Pair<String, Long>> parameters = List.of();
        StringBuilder sb = new StringBuilder();
        String format = "Parameter: %s, Value: %d\n";

        TopParametersRender.renderTopParameterTable(sb, parameters, 3, format);

        String expected = "";
        assertEquals(expected, sb.toString(), "The table should be empty when there are no parameters.");
    }
}
