package backend.academy.log.analyzer.service.cli.parser;

import backend.academy.log.analyzer.model.CLIArguments;

public interface CLIParser {
    CLIArguments parseArguments(String[] args);
}
