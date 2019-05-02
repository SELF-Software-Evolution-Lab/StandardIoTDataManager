package co.edu.uniandes.xrepo.service.reports;

import java.io.File;

import co.edu.uniandes.xrepo.domain.BatchTask;

public interface SearchReportFileLocator {

    File locateReportFile(BatchTask task);
}
