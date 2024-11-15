package backend.academy.log.analyzer.service.render.http.response.decoder;

import org.apache.hc.core5.http.ReasonPhraseCatalog;
import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;
import java.util.Locale;

public class HttpStatusDescription {
    private static final ReasonPhraseCatalog catalog = EnglishReasonPhraseCatalog.INSTANCE;

    public static String getStatusDescription(int statusCode) {
        return catalog.getReason(statusCode, Locale.ENGLISH);
    }
}
