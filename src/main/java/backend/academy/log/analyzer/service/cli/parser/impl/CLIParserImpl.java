package backend.academy.log.analyzer.service.cli.parser.impl;

import backend.academy.log.analyzer.model.CLIArguments;
import backend.academy.log.analyzer.service.cli.parser.CLIParser;
import java.util.HashMap;
import java.util.Map;

public class CLIParserImpl implements CLIParser {
    private static final String BEGINNING_OF_FLAG = "--";
    private static final String PATH_FLAG_VALUE = "path";
    private static final String FILTER_FIELD_FLAG_VALUE = "filter-field";
    private static final String FILTER_VALUE_FLAG_VALUE = "filter-value";
    private static final String DATA_FROM_FLAG_VALUE = "from";
    private static final String DATA_TO_FLAG_VALUE = "to";
    private static final String FORMAT_FLAG_VALUE = "format";

    @Override
    public CLIArguments parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length;) {
            if (args[i].startsWith(BEGINNING_OF_FLAG)) {
                String key = args[i].substring(2);
                String value = (i + 1 < args.length && !args[i + 1].startsWith(BEGINNING_OF_FLAG)) ? args[i + 1] : null;
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
            arguments.get(PATH_FLAG_VALUE),
            arguments.get(FILTER_FIELD_FLAG_VALUE),
            arguments.get(FILTER_VALUE_FLAG_VALUE),
            arguments.get(DATA_FROM_FLAG_VALUE),
            arguments.get(DATA_TO_FLAG_VALUE),
            arguments.get(FORMAT_FLAG_VALUE)
        );
    }
}
