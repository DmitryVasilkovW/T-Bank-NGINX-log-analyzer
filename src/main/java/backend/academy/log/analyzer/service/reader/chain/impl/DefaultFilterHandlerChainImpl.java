package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class DefaultFilterHandlerChainImpl extends FilterHandlerChainImpl {

    @Override
    public boolean handle(FilterRequest filterRequest) {
        return true;
    }
}
