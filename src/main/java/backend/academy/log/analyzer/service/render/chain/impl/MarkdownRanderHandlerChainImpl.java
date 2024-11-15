package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.impl.MarkdownReportRanderImpl;
import java.util.Optional;

public class MarkdownRanderHandlerChainImpl extends RanderHandlerChainImpl {
    private static final String MARKDOWN_TYPE = "markdown";
    private static final String SHORT_MARKDOWN_TYPE = "md";

    @Override
    public Optional<ReportRander> handle(RanderRequest request) {
        if (request.format().equals(MARKDOWN_TYPE)
        || request.format().equals(SHORT_MARKDOWN_TYPE)) {
            return Optional.of(new MarkdownReportRanderImpl());
        }
        return next.handle(request);
    }
}
