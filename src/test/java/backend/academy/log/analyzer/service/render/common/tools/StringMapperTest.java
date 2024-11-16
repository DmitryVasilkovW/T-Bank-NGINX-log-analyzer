package backend.academy.log.analyzer.service.render.common.tools;

import backend.academy.log.analyzer.model.Pair;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringMapperTest {

    @Test
    void testGetFiltrationAsString() {
        Pair<String, String> filtration = new Pair<>("key", "value");
        String result = StringMapper.getFiltrationAsString(filtration);
        String expected = "In key parameter to show only value";
        assertEquals(expected, result, "The filtration string should be formatted correctly.");

        result = StringMapper.getFiltrationAsString(null);
        expected = "No filtration";
        assertEquals(expected, result, "The filtration string should be 'No filtration' when the input is null.");
    }

    @Test
    void testGetDataAsString() {
        OffsetDateTime date = OffsetDateTime.now();
        String result = StringMapper.getDataAsString(date, "Some message");
        String expected = date.toString();
        assertEquals(expected, result, "The date string should be converted correctly.");

        result = StringMapper.getDataAsString(null, "Some message");
        expected = "Some message";
        assertEquals(expected, result, "The result should be the message when the date is null.");
    }

    @Test
    void testGetSourcesAsString() {
        List<String> sources = List.of("source1", "source2", "source3");
        String result = StringMapper.getSourcesAsString(sources);
        String expected = "source1, source2, source3";
        assertEquals(expected, result, "The sources string should be joined correctly with commas.");

        sources = List.of();
        result = StringMapper.getSourcesAsString(sources);
        expected = "";
        assertEquals(expected, result, "The result should be an empty string when the sources list is empty.");

        sources = List.of("singleSource");
        result = StringMapper.getSourcesAsString(sources);
        expected = "singleSource";
        assertEquals(expected, result,
            "The result should contain the single source when the list contains one element.");
    }
}
