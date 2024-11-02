package backend.academy.log.analyzer;

import java.util.Map;

public class Report {
    private final long totalRequests;
    private final Map<String, Long> resourceCount;
    private final Map<Integer, Long> statusCount;
    private final double averageResponseSize;
    private final long percentile95ResponseSize;

    public Report(long totalRequests, Map<String, Long> resourceCount, Map<Integer, Long> statusCount,
        double averageResponseSize, long percentile95ResponseSize) {
        this.totalRequests = totalRequests;
        this.resourceCount = resourceCount;
        this.statusCount = statusCount;
        this.averageResponseSize = averageResponseSize;
        this.percentile95ResponseSize = percentile95ResponseSize;
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public Map<String, Long> getResourceCount() {
        return resourceCount;
    }

    public Map<Integer, Long> getStatusCount() {
        return statusCount;
    }

    public double getAverageResponseSize() {
        return averageResponseSize;
    }

    public long getPercentile95ResponseSize() {
        return percentile95ResponseSize;
    }
}
