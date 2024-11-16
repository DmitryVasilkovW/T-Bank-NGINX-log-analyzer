package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class NoFilterHandlerChainImpl extends FilterHandlerChainImpl {

    @Override
    public boolean handle(FilterRequest filterRequest) {
        if (filterRequest.filtration().isEmpty()) {
            return true;
        }
        return next.handle(filterRequest);
    }
}
