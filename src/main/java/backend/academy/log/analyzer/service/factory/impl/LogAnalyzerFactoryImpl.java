package backend.academy.log.analyzer.service.factory.impl;

import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.factory.LogAnalyzerFactory;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import backend.academy.log.analyzer.service.reader.chain.factory.FilterHandlerChainFactory;
import backend.academy.log.analyzer.service.reader.impl.LogReaderImpl;

public class LogAnalyzerFactoryImpl implements LogAnalyzerFactory {
    private final FilterHandlerChainFactory filterHandlerChainFactory;

    public LogAnalyzerFactoryImpl(FilterHandlerChainFactory filterHandlerChainFactory) {
        this.filterHandlerChainFactory = filterHandlerChainFactory;
    }

    @Override
    public LogAnalyzer create() {
        var logparser = new LogParserImpl();
        var logReader = new LogReaderImpl(logparser, filterHandlerChainFactory.create());

        return new LogAnalyzerImpl(logReader);
    }
}
