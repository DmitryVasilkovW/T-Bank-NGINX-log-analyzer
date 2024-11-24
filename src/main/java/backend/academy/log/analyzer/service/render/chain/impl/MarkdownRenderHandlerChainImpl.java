package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.impl.MarkdownReportRenderImpl;
import java.util.Optional;

public class MarkdownRenderHandlerChainImpl extends RenderHandlerChainImpl {
    private static final String MARKDOWN_TYPE = "markdown";
    private static final String SHORT_MARKDOWN_TYPE = "md";

    @Override
    public Optional<ReportRender> handle(RenderRequest request) {
        if (request.format().equals(MARKDOWN_TYPE)
        || request.format().equals(SHORT_MARKDOWN_TYPE)) {
            return Optional.of(new MarkdownReportRenderImpl());
        }
        return next.handle(request);
    }
}
