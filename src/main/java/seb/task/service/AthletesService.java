package seb.task.service;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import seb.task.csv.CsvReader;
import seb.task.exceptions.SebResponseException;
import seb.task.model.AthleteResults;
import seb.task.common.Places;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static seb.task.messages.ErrorMessage.NO_FIRST_RESULT;

@Service
@Component
public class AthletesService {

    private final CsvReader csvReader;

    public AthletesService(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public List<AthleteResults> getAllAthletes() throws SebResponseException {
        List<AthleteResults> athleteResults;
        athleteResults = csvReader.readCSV();
        sort(athleteResults);
        concludingEventTime(athleteResults);
        finalScoreCalculation(athleteResults);
        return athleteResults;
    }

    private void sort(List<AthleteResults> athleteResults) {
        athleteResults.sort(Comparator.comparing(AthleteResults::getPoints).reversed());
    }

    private Long findingFirstTime(List<AthleteResults> athleteResults) throws SebResponseException {
        return athleteResults
                .stream()
                .filter(Objects::nonNull)
                .map(AthleteResults::getPoints)
                .findFirst()
                .orElseThrow(() -> SebResponseException.createValidation(NO_FIRST_RESULT));
    }

    private void concludingEventTime(List<AthleteResults> athleteResults) throws SebResponseException {
        long firstTime = findingFirstTime(athleteResults);

        for (AthleteResults result : athleteResults) {
            long diff = firstTime - result.getPoints();
            Duration duration = Duration.ofSeconds(diff);
            result.setConcludingEventTime(duration);
        }
    }

    private void finalScoreCalculation(List<AthleteResults> athleteResults) {

        for (AthleteResults time : athleteResults) {
            Duration concludingEventTime = time.getConcludingEventTime();
            Duration run = time.getRun();
            Duration finalTime = run.minus(concludingEventTime);
            time.setFinalTime(DurationFormatUtils.formatDurationHMS(finalTime.toMillis()));
        }
        athleteResults.sort(Comparator.comparing(AthleteResults::getFinalTime).reversed());

        AtomicInteger placeNumber = new AtomicInteger(1);
        athleteResults.forEach(athletes -> athletes.setFinalPlace(Places.findByNumber(placeNumber.getAndIncrement())));
    }

}
