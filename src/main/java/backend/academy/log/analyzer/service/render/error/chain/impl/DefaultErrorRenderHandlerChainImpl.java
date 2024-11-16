package backend.academy.log.analyzer.service.render.error.chain.impl;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.impl.TxtErrorRenderImpl;

public class DefaultErrorRenderHandlerChainImpl extends ErrorRenderHandlerChainImpl {

    @Override
    public ErrorRender handle(ErrorRequest request) {
        return new TxtErrorRenderImpl();
    }
}
