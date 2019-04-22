package co.edu.uniandes.xrepo.task.listener;

import java.time.LocalDate;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.xrepo.domain.SamplesFiles;
import co.edu.uniandes.xrepo.repository.SamplesFilesRepository;

public class JobCompletionListener extends JobExecutionListenerSupport {

	@Autowired
	SamplesFilesRepository samplesFilesRepository;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		String sampleFileId = jobExecution.getJobParameters().getString("SampleFileId");
		System.out.println("Sample File " +  sampleFileId + " Inicia procesado");
		updateSampleFileStatus(sampleFileId, 2, 0);


	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		String sampleFileId = jobExecution.getJobParameters().getString("SampleFileId");
		int recordsProcessed = 0;
		for(StepExecution stepExecution : jobExecution.getStepExecutions()){
			recordsProcessed += stepExecution.getReadCount();
		}
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("Sample File " +  sampleFileId + " procesado correctamente");
			updateSampleFileStatus(sampleFileId, 3, recordsProcessed);
		}
		else {
			System.out.println("Sample File " +  sampleFileId + " fallo procesado");
			updateSampleFileStatus(sampleFileId, 4, recordsProcessed);
		}
	}

	private void updateSampleFileStatus(String id, int status, int recordsProcessed) {
		if((id != null) && (id != "")) {
			SamplesFiles sampleFile = samplesFilesRepository.findById(id).get();
			if(sampleFile != null) {
				sampleFile.setUpdateDateTime(LocalDate.now());
				sampleFile.setState(status);
				sampleFile.setRecordsProcessed(recordsProcessed);
				samplesFilesRepository.save(sampleFile);
			}
		}
	}

}
