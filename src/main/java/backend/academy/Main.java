package backend.academy;

import backend.academy.log.analyzer.service.factory.impl.LogAnalyzerFactoryImpl;
import backend.academy.log.analyzer.service.launcher.factory.impl.CLILauncherFactoryImpl;
import backend.academy.log.analyzer.service.reader.chain.factory.impl.FilterHandlerChainFactoryImpl;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        new CLILauncherFactoryImpl(
            new LogAnalyzerFactoryImpl(
                new FilterHandlerChainFactoryImpl())
        )
            .create()
            .launch(args);
    }
}
