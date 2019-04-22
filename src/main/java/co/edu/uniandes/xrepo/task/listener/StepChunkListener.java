package co.edu.uniandes.xrepo.task.listener;

import java.time.LocalDate;

import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.xrepo.domain.SamplesFiles;
import co.edu.uniandes.xrepo.repository.SamplesFilesRepository;

public class StepChunkListener extends ChunkListenerSupport
{
    
    @Autowired
	SamplesFilesRepository samplesFilesRepository;

    @Override
    public void afterChunk(ChunkContext context) {
        
        int recordProcessed = context.getStepContext().getStepExecution().getReadCount();
        if ((recordProcessed % 1000) == 0) {
            String sampleFileId = context.getStepContext().getStepExecution().getJobParameters().getString("SampleFileId");

            if((sampleFileId != null) && (sampleFileId != "")) {
                SamplesFiles sampleFile = samplesFilesRepository.findById(sampleFileId).get();
                if(sampleFile != null) {
                    sampleFile.setUpdateDateTime(LocalDate.now());
                    sampleFile.setRecordsProcessed(recordProcessed);
                    samplesFilesRepository.save(sampleFile);
                }

                System.out.println(String.format("Se han procesado %d registros para Sample File %s", recordProcessed, sampleFileId));
            }

        }  
       
        super.afterChunk(context);
   }
}