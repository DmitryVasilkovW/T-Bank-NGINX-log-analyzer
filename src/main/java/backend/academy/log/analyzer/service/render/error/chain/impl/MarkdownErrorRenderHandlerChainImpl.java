package backend.academy.log.analyzer.service.render.error.chain.impl;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.impl.MarkdownErrorRenderImpl;

public class MarkdownErrorRenderHandlerChainImpl extends ErrorRenderHandlerChainImpl {
    private static final String MARKDOWN_FORMAT = "markdown";
    private static final String SHORT_MARKDOWN_FORMAT = "md";

    @Override
    public ErrorRender handle(ErrorRequest request) {
        if (request.format().equalsIgnoreCase(MARKDOWN_FORMAT)
            || request.format().equalsIgnoreCase(SHORT_MARKDOWN_FORMAT)) {
            return new MarkdownErrorRenderImpl();
        }

        return next.handle(request);
    }
}
