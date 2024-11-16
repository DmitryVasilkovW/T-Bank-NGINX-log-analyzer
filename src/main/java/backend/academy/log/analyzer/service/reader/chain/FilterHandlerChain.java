package backend.academy.log.analyzer.service.reader.chain;

import backend.academy.log.analyzer.model.FilterRequest;

public interface FilterHandlerChain {
    FilterHandlerChain addNext(FilterHandlerChain link);

    boolean handle(FilterRequest filterRequest);
}
