package co.edu.uniandes.xrepo.service.reports;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import co.edu.uniandes.xrepo.domain.Sample;

public class SampleCsvAdapter {

    private static final String CSV_DELIMITER = ",";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");

    private final Sample sample;


    public SampleCsvAdapter(Sample sample) {
        this.sample = sample;
    }

    public String toCsvRecord() {
        StringJoiner joiner = new StringJoiner(CSV_DELIMITER);
        Instant ts = sample.getTs();
        joiner.add(sample.getSamplingId())
            .add(sample.getDateTime().format(dateTimeFormatter))
            .add(ts.getEpochSecond() + "." + pad9(ts.getNano()))
            .add(sample.getSensorInternalId());
        sample.getMeasurements().forEach((var, measure) -> joiner.add(var.toLowerCase()).add(measure.toPlainString()));
        return joiner.toString();
    }

    private String pad9(int nano) {
        return String.format("%-9s", nano).replace(' ', '0');
    }
}
