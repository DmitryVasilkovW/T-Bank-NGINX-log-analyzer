package backend.academy.log.analyzer.service.render.error.chain.impl;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;
import backend.academy.log.analyzer.service.render.error.chain.ErrorRenderHandlerChain;

public abstract class ErrorRenderHandlerChainImpl implements ErrorRenderHandlerChain {
    protected ErrorRenderHandlerChain next;

    @Override
    public ErrorRenderHandlerChain addNext(ErrorRenderHandlerChain link) {
        if (next == null) {
            next = link;
        } else {
            next.addNext(link);
        }

        return this;
    }

    @Override
    public abstract ErrorRender handle(ErrorRequest request);
}
