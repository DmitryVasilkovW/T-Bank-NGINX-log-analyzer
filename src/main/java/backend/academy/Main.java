package backend.academy;

import backend.academy.log.analyzer.service.launcher.factory.CLILauncherFactory;
import backend.academy.log.analyzer.service.launcher.factory.impl.CLILauncherFactoryImpl;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        CLILauncherFactory factory = new CLILauncherFactoryImpl();
        factory.create().launch(args);
    }
}
