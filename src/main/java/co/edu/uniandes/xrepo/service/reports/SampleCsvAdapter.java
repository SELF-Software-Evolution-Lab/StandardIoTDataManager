package co.edu.uniandes.xrepo.service.reports;

import java.time.Instant;
import java.util.StringJoiner;

import co.edu.uniandes.xrepo.domain.Sample;

public class SampleCsvAdapter {

    private static final String CSV_DELIMITER = ",";

    private final Sample sample;


    public SampleCsvAdapter(Sample sample) {
        this.sample = sample;
    }

    public String toCsvRecord() {
        StringJoiner joiner = new StringJoiner(CSV_DELIMITER);
        Instant ts = sample.getTs();
        joiner.add(sample.getSamplingId())
            .add(sample.getDateTime().toString())
            .add(ts.getEpochSecond() + "." + ts.getNano())
            .add(sample.getSensorInternalId());
        sample.getMeasurements().forEach((var, measure) -> joiner.add(var.toLowerCase()).add(measure.toPlainString()));
        return joiner.toString();
    }
}
