package backend.academy.log.analyzer.service.render.http.response.decoder;

import java.util.Locale;
import lombok.experimental.UtilityClass;
import org.apache.hc.core5.http.ReasonPhraseCatalog;
import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;

@UtilityClass
public final class HttpStatusDescription {
    private static final ReasonPhraseCatalog ENGLISH_REASON_PHRASE_CATALOG = EnglishReasonPhraseCatalog.INSTANCE;

    public static String getStatusDescription(int statusCode) {
        return ENGLISH_REASON_PHRASE_CATALOG.getReason(statusCode, Locale.ENGLISH);
    }
}
