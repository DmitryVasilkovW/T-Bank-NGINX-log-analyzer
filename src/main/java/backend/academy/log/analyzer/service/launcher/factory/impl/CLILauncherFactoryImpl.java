package backend.academy.log.analyzer.service.launcher.factory.impl;

import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.cli.parser.impl.CLIParserImpl;
import backend.academy.log.analyzer.service.cli.printer.impl.PrintStreamPrinterImpl;
import backend.academy.log.analyzer.service.factory.impl.LogAnalyzerFactoryImpl;
import backend.academy.log.analyzer.service.launcher.CLILauncher;
import backend.academy.log.analyzer.service.launcher.factory.CLILauncherFactory;
import backend.academy.log.analyzer.service.launcher.impl.CLILauncherImpl;
import backend.academy.log.analyzer.service.reader.chain.factory.impl.FilterHandlerChainFactoryImpl;
import backend.academy.log.analyzer.service.render.chain.factory.impl.RenderHandlerChainFactoryImpl;
import backend.academy.log.analyzer.service.render.error.chain.factory.impl.ErrorRenderHandlerChainFactoryImpl;

public class CLILauncherFactoryImpl implements CLILauncherFactory {

    @Override
    public CLILauncher create() {
        var cliParser = new CLIParserImpl();
        var filterFactory = new FilterHandlerChainFactoryImpl();
        LogAnalyzer logAnalyzer = new LogAnalyzerFactoryImpl(filterFactory).create();
        var printer = new PrintStreamPrinterImpl(System.out);
        var errorFactory = new ErrorRenderHandlerChainFactoryImpl();
        var renderFactory = new RenderHandlerChainFactoryImpl();

        return new CLILauncherImpl(cliParser, logAnalyzer, printer, errorFactory, renderFactory);
    }
}
