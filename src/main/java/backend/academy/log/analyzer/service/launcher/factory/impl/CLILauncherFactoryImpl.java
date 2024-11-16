package backend.academy.log.analyzer.service.launcher.factory.impl;

import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.cli.parser.impl.CLIParserImpl;
import backend.academy.log.analyzer.service.factory.LogAnalyzerFactory;
import backend.academy.log.analyzer.service.launcher.CLILauncher;
import backend.academy.log.analyzer.service.launcher.factory.CLILauncherFactory;
import backend.academy.log.analyzer.service.launcher.impl.CLILauncherImpl;

public class CLILauncherFactoryImpl implements CLILauncherFactory {
    private final LogAnalyzerFactory logAnalyzerFactory;

    public CLILauncherFactoryImpl(LogAnalyzerFactory logAnalyzerFactory) {
        this.logAnalyzerFactory = logAnalyzerFactory;
    }

    @Override
    public CLILauncher create() {
        var cliParser = new CLIParserImpl();
        LogAnalyzer logAnalyzer = logAnalyzerFactory.create();

        return new CLILauncherImpl(cliParser, logAnalyzer);
    }
}
