package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.impl.AdocReportRenderImpl;
import java.util.Optional;

public class AdocRenderHandlerChainImpl extends RenderHandlerChainImpl {
    private static final String ADOC_TYPE = "adoc";

    @Override
    public Optional<ReportRender> handle(RanderRequest request) {
        if (request.format().equals(ADOC_TYPE)) {
            return Optional.of(new AdocReportRenderImpl());
        }
        return next.handle(request);
    }
}
