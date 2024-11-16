package backend.academy;

import backend.academy.log.analyzer.service.launcher.CLILauncher;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        new CLILauncher().launch(args);
    }
}
