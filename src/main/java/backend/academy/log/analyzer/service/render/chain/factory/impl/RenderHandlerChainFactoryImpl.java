package backend.academy.log.analyzer.service.render.chain.factory.impl;

import backend.academy.log.analyzer.service.render.chain.RenderHandlerChain;
import backend.academy.log.analyzer.service.render.chain.factory.RenderHandlerChainFactory;
import backend.academy.log.analyzer.service.render.chain.impl.AdocRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.chain.impl.DefaultRenderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.chain.impl.MarkdownRenderHandlerChainImpl;

public class RenderHandlerChainFactoryImpl implements RenderHandlerChainFactory {

    @Override
    public RenderHandlerChain create() {
        return new MarkdownRenderHandlerChainImpl()
            .addNext(new AdocRenderHandlerChainImpl()
                .addNext(new DefaultRenderHandlerChainImpl()));
    }
}
