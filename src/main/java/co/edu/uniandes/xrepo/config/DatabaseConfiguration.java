package co.edu.uniandes.xrepo.config;

import java.net.MalformedURLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import io.github.jhipster.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;

@Configuration
@EnableMongoRepositories("co.edu.uniandes.xrepo.repository")
@Profile("!" + JHipsterConstants.SPRING_PROFILE_CLOUD)
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    public static final String EPOCH_SECOND_FIELD = "epochSecond";
    public static final String NANO_FIELD = "nano";

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        converters.add(InstantWriterConverter.INSTANCE);
        converters.add(InstantReaderConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @Bean
    public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
        log.debug("Configuring Mongobee");
        Mongobee mongobee = new Mongobee(mongoClient);
        mongobee.setDbName(mongoProperties.getMongoClientDatabase());
        mongobee.setMongoTemplate(mongoTemplate);
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage("co.edu.uniandes.xrepo.config.dbmigrations");
        mongobee.setEnabled(true);
        return mongobee;
    }

    @Value("classpath:org/springframework/batch/core/schema-drop-postgresql.sql")
    private Resource dropReopsitoryTables;
 
    @Value("classpath:org/springframework/batch/core/schema-postgresql.sql")
    private Resource dataReopsitorySchema;
    
    @Bean(name="batchDataSource")
    public DataSource batchDataSource(){
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/batch_processing")
                .driverClassName("org.postgresql.Driver")
                .username("postgres")
                .password("postgres")
                .build();          
    }
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource)
      throws MalformedURLException {
        ResourceDatabasePopulator databasePopulator = 
          new ResourceDatabasePopulator();
 
        databasePopulator.addScript(dropReopsitoryTables);
        databasePopulator.addScript(dataReopsitorySchema);
        databasePopulator.setIgnoreFailedDrops(true);
 
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }
    
    @WritingConverter
    enum InstantWriterConverter implements Converter<Instant, Document> {

        INSTANCE;

        @Override
        public Document convert(Instant instant) {
            Document document = new Document();
            document.put(EPOCH_SECOND_FIELD, Long.valueOf(instant.getEpochSecond()));
            document.put(NANO_FIELD, Long.valueOf(instant.getNano()));
            return document;
        }
    }

    @ReadingConverter
    enum InstantReaderConverter implements Converter<Document, Instant> {

        INSTANCE;

        @Override
        public Instant convert(Document document) {
            return Instant.ofEpochSecond(Long.valueOf(document.get(EPOCH_SECOND_FIELD).toString()),
                Long.valueOf(document.get(NANO_FIELD).toString()));
        }
    }
}
