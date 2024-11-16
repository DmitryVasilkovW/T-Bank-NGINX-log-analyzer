package backend.academy;

import backend.academy.log.analyzer.service.factory.impl.LogAnalyzerFactoryImpl;
import backend.academy.log.analyzer.service.launcher.factory.impl.CLILauncherFactoryImpl;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        new CLILauncherFactoryImpl(
            new LogAnalyzerFactoryImpl())
            .create()
            .launch(args);
    }
}
