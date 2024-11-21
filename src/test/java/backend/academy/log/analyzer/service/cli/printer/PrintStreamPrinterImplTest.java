package backend.academy.log.analyzer.service.cli.printer;

import backend.academy.log.analyzer.service.cli.printer.impl.PrintStreamPrinterImpl;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PrintStreamPrinterImplTest {
    @Mock
    private PrintStream mockPrintStream;

    @InjectMocks
    private PrintStreamPrinterImpl printer;

    @Test
    void testPrintlnShouldPrintMessageToStream() {
        String message = "Hello, world!";

        printer.println(message);

        verify(mockPrintStream).println(message);
    }

    @Test
    void testPrintlnShouldHandleNullMessage() {
        printer.println(null);

        verify(mockPrintStream).println(((String) null));
    }
}
