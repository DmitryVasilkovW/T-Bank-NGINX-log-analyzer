package backend.academy.log.analyzer.service.cli.parser.impl;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import java.util.HashMap;
import java.util.Map;

public class CLIParserImpl implements CLIParser {

    @Override
    public CLIArguments parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length;) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2);
                String value = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[i + 1] : null;
                arguments.put(key, value);
                i += (value != null) ? 2 : 1;
            } else {
                i++;
            }
        }
        return formatArguments(arguments);
    }

    private CLIArguments formatArguments(Map<String, String> arguments) {
        return new CLIArguments(
            arguments.get("path"),
            arguments.get("filter-field"),
            arguments.get("filter-value"),
            arguments.get("from"),
            arguments.get("to"),
            arguments.get("format")
        );
    }
}

