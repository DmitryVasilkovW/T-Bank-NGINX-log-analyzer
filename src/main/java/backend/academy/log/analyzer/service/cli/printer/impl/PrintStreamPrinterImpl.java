package backend.academy.log.analyzer.service.cli.printer.impl;

import backend.academy.log.analyzer.service.cli.printer.Printer;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrintStreamPrinterImpl implements Printer {
    private final PrintStream printStream;

    @Override
    public void println(String message) {
        printStream.println(message);
    }
}
