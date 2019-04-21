package co.edu.uniandes.xrepo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.repository.SampleRepository;
import co.edu.uniandes.xrepo.repository.SamplesFilesRepository;
import co.edu.uniandes.xrepo.task.fieldsmapper.SampleFieldSetMapper;
import co.edu.uniandes.xrepo.task.listener.JobCompletionListener;
import co.edu.uniandes.xrepo.task.step.Writer;

@Configuration
public class BatchConfig {

	private static final String OVERRIDDEN_BY_EXPRESSION = null;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public SampleRepository sampleRepository;

	@Autowired
	public SamplesFilesRepository sampleFilesRepository;
	
	@Bean
	public Job processJob() {
		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(listener())
				.flow(orderStep1()).end().build();
	}

	@Bean
	public Step orderStep1() {
		return stepBuilderFactory.get("orderStep1").<Sample, Sample>chunk(1).reader(itemReader(OVERRIDDEN_BY_EXPRESSION)).writer(new Writer(sampleRepository))
				.build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}

	@Bean
	BatchConfigurer configurer(@Qualifier("batchDataSource") DataSource dataSource) {
		return new DefaultBatchConfigurer(dataSource) {
			@Override
			public JobLauncher getJobLauncher() {
				final SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
				jobLauncher.setJobRepository(getJobRepository());
				final SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
				jobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);
				try {
					jobLauncher.afterPropertiesSet();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return jobLauncher;
			}
		};
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Sample> itemReader (@Value("#{jobParameters[filePath]}") String filePath) {
		FlatFileItemReader<Sample> reader = new FlatFileItemReader<Sample>();
		reader.setResource(new FileSystemResource(filePath));
		reader.setName("sampleItemReader");
		reader.setLineMapper(snidLineMapper());
		return(reader);	
	}

	@Bean
    public LineMapper<Sample> snidLineMapper() {
        DefaultLineMapper<Sample> lineMapper = new DefaultLineMapper<Sample>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(true);
        lineTokenizer.setStrict(true);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new SampleFieldSetMapper());
        return lineMapper;
    }

}

