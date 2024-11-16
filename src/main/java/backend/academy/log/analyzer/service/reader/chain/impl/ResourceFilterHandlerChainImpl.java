package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class ResourceFilterHandlerChainImpl extends FilterHandlerChainImpl {
    private static final String RESOURCE_NAME = "resource";

    @Override
    public boolean handle(FilterRequest filterRequest) {
        String filtrationParameter = filterRequest.filtration().get().first();
        String filtrationValue = filterRequest.filtration().get().second();
        String resource = filterRequest.logRecord().resource();

        if (filtrationParameter.equals(RESOURCE_NAME)) {
            return resource != null && resource.contains(filtrationValue);
        }

        return next.handle(filterRequest);
    }
}
