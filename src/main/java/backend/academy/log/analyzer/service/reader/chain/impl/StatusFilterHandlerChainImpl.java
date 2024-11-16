package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class StatusFilterHandlerChainImpl extends FilterHandlerChainImpl {
    private static final String STATUS_NAME = "status";

    @Override
    public boolean handle(FilterRequest filterRequest) {
        String filtrationParameter = filterRequest.filtration().get().first();
        String filtrationValue = filterRequest.filtration().get().second();
        int status = filterRequest.logRecord().statusCode();

        if (filtrationParameter.equals(STATUS_NAME)) {
            try {
                int statusCode = Integer.parseInt(filtrationValue);
                return status == statusCode;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return next.handle(filterRequest);
    }
}
