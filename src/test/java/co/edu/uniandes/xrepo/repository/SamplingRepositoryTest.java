package co.edu.uniandes.xrepo.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.edu.uniandes.xrepo.XrepoApp;
import co.edu.uniandes.xrepo.domain.Sampling;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SamplingRepositoryTest {

    @Autowired
    private SamplingRepository samplingRepository;

    @Before
    public void setUp() {
        samplingRepository.deleteAll();

        samplingRepository.save(
            new Sampling().name("sampling test")
                .tags(Arrays.asList("tag1", "tag2"))
        );
    }

    @Test
    public void findByTags() {
        assertThat(samplingRepository.findAll()).hasSize(1);
        List<Sampling> withTags =
            samplingRepository.findWithTags(Arrays.asList("tag2"));
        assertThat(withTags).hasSize(1);
    }

    @Test
    public void findByTagsMultiple() {
        assertThat(samplingRepository.findAll()).hasSize(1);
        List<Sampling> withTags =
            samplingRepository.findWithTags(Arrays.asList("tag2", "tag1"));
        assertThat(withTags).hasSize(1);
    }

    @Test
    public void findByTagsNotMatch() {
        assertThat(samplingRepository.findAll()).hasSize(1);
        List<Sampling> withTags =
            samplingRepository.findWithTags(Arrays.asList("tag3"));
        assertThat(withTags).hasSize(0);
    }

    @Test
    public void findByTagsNotMatchMultiple() {
        assertThat(samplingRepository.findAll()).hasSize(1);
        List<Sampling> withTags =
            samplingRepository.findWithTags(Arrays.asList("tag1", "tag3"));
        assertThat(withTags).hasSize(0);
    }
}
