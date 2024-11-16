package backend.academy.log.analyzer.service.cli.printer.impl;

import backend.academy.log.analyzer.service.cli.printer.Printer;
import java.io.PrintStream;

public class PrintStreamPrinterImpl implements Printer {
    private final PrintStream printStream;

    public PrintStreamPrinterImpl(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void println(String message) {
        printStream.println(message);
    }
}
