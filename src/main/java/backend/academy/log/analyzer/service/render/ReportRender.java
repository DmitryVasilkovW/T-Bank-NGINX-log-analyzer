package backend.academy.log.analyzer.service.render;

import backend.academy.log.analyzer.model.Report;

public interface ReportRender {
    String renderReportAsString(Report report);
}
