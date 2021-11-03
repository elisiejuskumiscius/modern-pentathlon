package seb.task.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import seb.task.csv.CsvReader;
import seb.task.model.AthleteResults;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Service
@Component
public class AthletesService {

    private final CsvReader csvReader;

    public AthletesService(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public List<AthleteResults> getAllAthletes() {
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

    private void concludingEventTime(List<AthleteResults> athleteResults) {

        long firstTime = athleteResults
                .stream()
                .map(AthleteResults::getPoints)
                .findFirst()
                .get();

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
            time.setFinalTime(finalTime);
        }
        athleteResults.sort(Comparator.comparing(AthleteResults::getFinalTime).reversed());

        int place = 1;
        for (AthleteResults places : athleteResults) {
            places.setFinalPlace(place);
            place++;
        }

    }

}
