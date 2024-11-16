package backend.academy.log.analyzer.service.cli.parser;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.service.cli.parser.impl.CLIParserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CLIParserImplTest {
    private final CLIParserImpl parser = new CLIParserImpl();

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
