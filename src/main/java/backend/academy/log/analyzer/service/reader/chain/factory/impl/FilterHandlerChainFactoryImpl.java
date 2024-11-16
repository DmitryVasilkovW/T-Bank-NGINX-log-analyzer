package backend.academy.log.analyzer.service.reader.chain.factory.impl;

import backend.academy.log.analyzer.service.reader.chain.FilterHandlerChain;
import backend.academy.log.analyzer.service.reader.chain.factory.FilterHandlerChainFactory;
import backend.academy.log.analyzer.service.reader.chain.impl.AgentFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.DefaultFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.IpFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.MethodFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.NoFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.ResourceFilterHandlerChainImpl;
import backend.academy.log.analyzer.service.reader.chain.impl.StatusFilterHandlerChainImpl;

public class FilterHandlerChainFactoryImpl implements FilterHandlerChainFactory {

    @Override
    public FilterHandlerChain create() {
        return new NoFilterHandlerChainImpl()
            .addNext(new AgentFilterHandlerChainImpl()
                .addNext(new MethodFilterHandlerChainImpl()
                    .addNext(new ResourceFilterHandlerChainImpl()
                        .addNext(new StatusFilterHandlerChainImpl()
                            .addNext(new IpFilterHandlerChainImpl()
                                .addNext(new DefaultFilterHandlerChainImpl())))))
                );
    }
}
