package backend.academy.log.analyzer.service.factory.impl;

import backend.academy.log.analyzer.service.LogAnalyzer;
import backend.academy.log.analyzer.service.factory.LogAnalyzerFactory;
import backend.academy.log.analyzer.service.impl.LogAnalyzerImpl;
import backend.academy.log.analyzer.service.parser.impl.LogParserImpl;
import backend.academy.log.analyzer.service.reader.LogReaderImpl;

public class LogAnalyzerFactoryImpl implements LogAnalyzerFactory {

    @Override
    public LogAnalyzer create() {
        var logparser = new LogParserImpl();
        var logReader = new LogReaderImpl(logparser);

        return new LogAnalyzerImpl(logReader);
    }
}
