package co.edu.uniandes.xrepo.service.reports;

import java.util.StringJoiner;

import co.edu.uniandes.xrepo.domain.Sample;

public class SampleCsvAdapter {

    private final Sample sample;

    public SampleCsvAdapter(Sample sample) {
        this.sample = sample;
    }

    public String toCsvRecord(){
        StringJoiner joiner = new StringJoiner(",");

        return joiner.toString();
    }
}
