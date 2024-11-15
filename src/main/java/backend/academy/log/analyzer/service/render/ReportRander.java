package backend.academy.log.analyzer.service.render;

import backend.academy.log.analyzer.model.Report;

public interface ReportRander {
    String randerReportAsString(Report report);
}
