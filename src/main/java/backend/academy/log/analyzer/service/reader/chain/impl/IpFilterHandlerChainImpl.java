package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class IpFilterHandlerChainImpl extends FilterHandlerChainImpl {
    private static final String IP_NAME = "ip";

    @Override
    public boolean handle(FilterRequest filterRequest) {
        String filtrationParameter = filterRequest.filtration().get().first();
        String filtrationValue = filterRequest.filtration().get().second();
        String ip = filterRequest.logRecord().remoteAddr();

        if (filtrationParameter.equals(IP_NAME)) {
            return ip != null && ip.equals(filtrationValue);
        }

        return next.handle(filterRequest);
    }
}
