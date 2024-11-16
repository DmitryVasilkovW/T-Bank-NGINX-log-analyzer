package backend.academy.log.analyzer.service.render.http.response.decoder;

import java.util.Locale;
import org.apache.hc.core5.http.ReasonPhraseCatalog;
import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;

public final class HttpStatusDescription {
    private static final ReasonPhraseCatalog ENGLISH_REASON_PHRASE_CATALOG = EnglishReasonPhraseCatalog.INSTANCE;

    private HttpStatusDescription() {
    }

    public static String getStatusDescription(int statusCode) {
        return ENGLISH_REASON_PHRASE_CATALOG.getReason(statusCode, Locale.ENGLISH);
    }
}
