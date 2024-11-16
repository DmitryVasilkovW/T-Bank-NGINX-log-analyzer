package backend.academy.log.analyzer.service.cli.printer;

import backend.academy.log.analyzer.service.cli.printer.impl.PrintStreamPrinterImpl;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import static org.assertj.core.api.Assertions.assertThat;

class PrintStreamPrinterImplTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    @Spy
    private PrintStream printStream = new PrintStream(outputStream);
    @Spy
    private PrintStreamPrinterImpl printer = new PrintStreamPrinterImpl(printStream);

    @Test
    void testPrintlnShouldPrintMessageToStream() {
        String message = "Hello, world!";

        printer.println(message);

        assertThat(outputStream.toString()).isEqualTo(message + System.lineSeparator());
    }

    @Test
    void testPrintlnShouldHandleNullMessage() {
        printer.println(null);

        assertThat(outputStream.toString()).isEqualTo("null" + System.lineSeparator());
    }
}
