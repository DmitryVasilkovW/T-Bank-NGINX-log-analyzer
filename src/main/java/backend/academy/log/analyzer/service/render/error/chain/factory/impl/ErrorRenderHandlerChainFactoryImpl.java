package backend.academy.log.analyzer.service.render.error.chain.factory.impl;

import backend.academy.log.analyzer.service.render.error.chain.ErrorRenderHandlerChain;
import backend.academy.log.analyzer.service.render.error.chain.factory.ErrorRenderHandlerChainFactory;
import backend.academy.log.analyzer.service.render.error.chain.impl.AdocErrorRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.error.chain.impl.DefaultErrorRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.error.chain.impl.MarkdownErrorRenderHandlerChainImpl;

public class ErrorRenderHandlerChainFactoryImpl implements ErrorRenderHandlerChainFactory {

    @Override
    public ErrorRenderHandlerChain create() {
        return new MarkdownErrorRenderHandlerChainImpl()
            .addNext(new AdocErrorRenderHandlerChainImpl()
                .addNext(new DefaultErrorRenderHandlerChainImpl()));
    }
}
