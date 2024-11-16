package backend.academy.log.analyzer.service.reader.chain.impl;

import backend.academy.log.analyzer.model.FilterRequest;

public class AgentFilterHandlerChainImpl extends FilterHandlerChainImpl {
    private static final String AGENT_NAME = "agent";

    @Override
    public boolean handle(FilterRequest filterRequest) {
        String filtrationParameter = filterRequest.filtration().get().first();
        String filtrationValue = filterRequest.filtration().get().second();
        String agent = filterRequest.logRecord().httpUserAgent();

        if (filtrationParameter.equals(AGENT_NAME)) {
            return agent != null && agent.startsWith(filtrationValue);
        }

        return next.handle(filterRequest);
    }
}
