package backend.academy.log.analyzer.service.render.chain.factory.impl;

import backend.academy.log.analyzer.service.render.chain.RanderHandlerChain;
import backend.academy.log.analyzer.service.render.chain.factory.RanderHandlerChainFactory;
import backend.academy.log.analyzer.service.render.chain.impl.AdocRanderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.chain.impl.DefaultRanderHandlerChainImpl;
import backend.academy.log.analyzer.service.render.chain.impl.MarkdownRanderHandlerChainImpl;

public class RanderHandlerChainFactoryImpl implements RanderHandlerChainFactory {

    @Override
    public RanderHandlerChain create() {
        return new MarkdownRanderHandlerChainImpl()
            .addNext(new AdocRanderHandlerChainImpl()
                .addNext(new DefaultRanderHandlerChainImpl()));
    }
}
