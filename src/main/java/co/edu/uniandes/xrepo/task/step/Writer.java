package co.edu.uniandes.xrepo.task.step;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.repository.SampleRepository;

public class Writer implements ItemWriter<Sample> {
    private final SampleRepository sampleRepository;
    
    public Writer(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }
	@Override
	public void write(List<? extends Sample> samples) throws Exception {
		for (Sample sample : samples) {
            sampleRepository.save(sample);
		}
	}

}