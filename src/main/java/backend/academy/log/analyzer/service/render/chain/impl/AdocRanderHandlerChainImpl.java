package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.impl.AdocReportRanderImpl;
import java.util.Optional;

public class AdocRanderHandlerChainImpl extends RanderHandlerChainImpl {
    private static final String ADOC_TYPE = "adoc";

    @Override
    public Optional<ReportRander> handle(RanderRequest request) {
        if (request.format().equals(ADOC_TYPE)) {
            return Optional.of(new AdocReportRanderImpl());
        }
        return next.handle(request);
    }
}
