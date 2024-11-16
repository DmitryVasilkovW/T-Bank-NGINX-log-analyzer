package backend.academy.log.analyzer.service.render.error.chain.impl;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.impl.AdocErrorRenderImpl;

public class AdocErrorRenderHandlerChainImpl extends ErrorRenderHandlerChainImpl {
    private static final String ADOC_FORMAT = "adoc";

    @Override
    public ErrorRender handle(ErrorRequest request) {
        if (request.format().equalsIgnoreCase(ADOC_FORMAT)) {
            return new AdocErrorRenderImpl();
        }

        return next.handle(request);
    }
}
