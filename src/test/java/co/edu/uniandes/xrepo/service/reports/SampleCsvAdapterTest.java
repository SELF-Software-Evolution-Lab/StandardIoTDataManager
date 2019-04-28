package co.edu.uniandes.xrepo.service.reports;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import co.edu.uniandes.xrepo.domain.Sample;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SampleCsvAdapterTest {

    private static final String REFERENCE_TS = "2019-04-27T10:11:59.382Z";
    private static final String REFERENCE_DATE = "2019-04-27T10:11:59.382";

    private static final String EXPECTED_RECORD = "SamplingId,2019-04-27T10:11:59.382,1556359919.382000000,sensorId," +
        "var1,0,var2,789465.789456,var3,77777777777.22222222";

    @Test
    public void convertToCSVRecord() {
        Sample sample = Sample.builder()
            .samplingId("SamplingId")
            .dateTime(LocalDateTime.parse(REFERENCE_DATE))
            .ts(Instant.parse(REFERENCE_TS))
            .sensorInternalId("sensorId")
            .measurements(ImmutableMap.of("var1", BigDecimal.ZERO,
                "var2", BigDecimal.valueOf(789465.789456),
                "var3", new BigDecimal("77777777777.22222222"))
            ).build();
        String msg = new SampleCsvAdapter(sample).toCsvRecord();
        assertThat(msg).isEqualTo(EXPECTED_RECORD);
    }
}
