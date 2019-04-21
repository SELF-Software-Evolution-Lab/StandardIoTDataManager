package co.edu.uniandes.xrepo.task.fieldsmapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import co.edu.uniandes.xrepo.domain.Sample;

public class SampleFieldSetMapper implements FieldSetMapper<Sample> {
    public Sample mapFieldSet(FieldSet fieldSet) {
        Sample sample = new Sample();

        sample.setSamplingId(fieldSet.readString(0));
        sample.setTs(Instant.ofEpochSecond((long)fieldSet.readDouble(2), (long)Math.rint((fieldSet.readDouble(2) * 1000000000) - (((long)fieldSet.readDouble(2)) * 1000000000))));
        sample.setSensorInternalId(fieldSet.readString(3));

        Map<String, BigDecimal> measurements = new HashMap<String,BigDecimal>();
        int totalmeasurements = (fieldSet.getFieldCount() - 4) / 2;
        int posColumn = 4;
        for(int i = 0; i < totalmeasurements; i++, posColumn += 2) {
            measurements.put(fieldSet.readString(posColumn), fieldSet.readBigDecimal(posColumn + 1));
        }
        sample.setMeasurements(measurements);
        return sample;
    }
}