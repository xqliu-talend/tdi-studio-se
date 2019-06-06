package org.talend.job.audit;

import org.talend.logging.audit.AuditLoggerFactory;
import org.talend.logging.audit.Context;

public class JobAuditLoggerTest {

	public static void main(String[] args) {
		final JobAuditLogger logger = AuditLoggerFactory.getEventAuditLogger(JobAuditLogger.class);
		Context context = JobContextBuilder.create().jobName("fetch_from_s3_every_day").jobId("jobid_123")
				.jobVersion("0.1").connectorType("tXMLMAP").connectorId("tXMLMap_1")
				.connectionName("row1").connectionType("reject").duration("20s")
				.rows(10).timestamp("2019-12-12 23:23:23.111+08:00").status("end").build();

		logger.jobstart(context);
		logger.jobstop(context);
		logger.runcomponent(context);
		logger.flowInput(context);
		logger.flowOutput(context);
	}
}
