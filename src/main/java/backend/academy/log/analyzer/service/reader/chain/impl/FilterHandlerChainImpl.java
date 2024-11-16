package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;
import backend.academy.log.analyzer.service.reader.chain.FilterHandlerChain;

public abstract class FilterHandlerChainImpl implements FilterHandlerChain {
    protected FilterHandlerChain next;

    @Override
    public FilterHandlerChain addNext(FilterHandlerChain link) {
        if (next == null) {
            next = link;
        } else {
            next.addNext(link);
        }

        return this;
    }

    public abstract boolean handle(FilterRequest filterRequest);
}
