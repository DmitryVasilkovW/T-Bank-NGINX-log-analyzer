package backend.academy.log.analyzer.service.cli.parser;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.service.cli.parser.impl.CLIParserImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CLIParserImplTest {
    @Mock
    private CLIParserImpl parser;

    @Test
    void testParseArgumentsShouldParseAllArgumentsCorrectly() {
        String[] args = {
            "--path", "/logs/app.log",
            "--filter-field", "status",
            "--filter-value", "error",
            "--from", "2024-01-01",
            "--to", "2024-12-31",
            "--format", "md"
        };

        CLIArguments mockResult =
            new CLIArguments("/logs/app.log", "status", "error", "2024-01-01", "2024-12-31", "md");
        when(parser.parseArguments(args)).thenReturn(mockResult);

        CLIArguments result = parser.parseArguments(args);

        assertNotNull(result);
        assertEquals("/logs/app.log", result.path());
        assertEquals("status", result.filterField());
        assertEquals("error", result.filterValue());
        assertEquals("2024-01-01", result.dataFrom());
        assertEquals("2024-12-31", result.dataTo());
        assertEquals("md", result.format());
    }

    @Test
    void testParseArgumentsShouldHandleMissingValues() {
        String[] args = {
            "--path", "/logs/app.log",
            "--filter-field"
        };

        CLIArguments mockResult = new CLIArguments("/logs/app.log", null, null, null, null, null);
        when(parser.parseArguments(args)).thenReturn(mockResult);

        CLIArguments result = parser.parseArguments(args);

        assertNotNull(result);
        assertEquals("/logs/app.log", result.path());
        assertNull(result.filterField());
        assertNull(result.filterValue());
        assertNull(result.dataFrom());
        assertNull(result.dataTo());
        assertNull(result.format());
    }

    @Test
    void testParseArgumentsShouldHandleEmptyArguments() {
        String[] args = {};

        CLIArguments mockResult = new CLIArguments(null, null, null, null, null, null);
        when(parser.parseArguments(args)).thenReturn(mockResult);

        CLIArguments result = parser.parseArguments(args);

        assertNotNull(result);
        assertNull(result.path());
        assertNull(result.filterField());
        assertNull(result.filterValue());
        assertNull(result.dataFrom());
        assertNull(result.dataTo());
        assertNull(result.format());
    }

    @Test
    void testParseArgumentsShouldIgnoreArgumentsWithoutDashes() {
        String[] args = {
            "random", "--path", "/logs/app.log", "invalid"
        };

        CLIArguments mockResult = new CLIArguments("/logs/app.log", null, null, null, null, null);
        when(parser.parseArguments(args)).thenReturn(mockResult);

        CLIArguments result = parser.parseArguments(args);

        assertNotNull(result);
        assertEquals("/logs/app.log", result.path());
        assertNull(result.filterField());
        assertNull(result.filterValue());
        assertNull(result.dataFrom());
        assertNull(result.dataTo());
        assertNull(result.format());
    }
}
