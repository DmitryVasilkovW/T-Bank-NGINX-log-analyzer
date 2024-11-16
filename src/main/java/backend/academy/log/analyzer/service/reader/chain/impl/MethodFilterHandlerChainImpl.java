package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class MethodFilterHandlerChainImpl extends FilterHandlerChainImpl {
    private static final String METHOD_NAME = "method";

    @Override
    public boolean handle(FilterRequest filterRequest) {
        String filtrationParameter = filterRequest.filtration().orElseThrow(RuntimeException::new).first();
        String filtrationValue = filterRequest.filtration().get().second();
        String method = filterRequest.logRecord().method();

        if (filtrationParameter.equals(METHOD_NAME)) {
            return method != null && method.equalsIgnoreCase(filtrationValue);
        }

        return next.handle(filterRequest);
    }
}
