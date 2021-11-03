package seb.task.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import seb.task.model.AthleteResults;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvReader {

    private static final Logger log = LoggerFactory.getLogger(CsvReader.class);

    public List<AthleteResults> readCSV() {
        List<AthleteResults> athleteResults = new ArrayList<>();
        File csvFile = new File("Athlete_Results.csv");

        try {
            int recordSize = 8;
            int athletesCount = 10;
            Files.readAllLines(csvFile.toPath(), StandardCharsets.UTF_8)
                    .forEach(line -> {
                        List<String> record = new ArrayList<>(recordSize);
                        record.addAll(Arrays.asList(line.split(",")));
                        while (record.size() < recordSize) {
                            record.add("");
                        }

                        athleteResults.add(parseAthleteResults(record, athletesCount));
                    });

        } catch (IOException e) {
            log.error("Reading CSV Error!", e);
        }

        log.info("Athlete_Results.csv read successfully");

        return athleteResults;
    }


    private AthleteResults parseAthleteResults(List<String> record, int athletesCount) {
        String fullName = record.get(0);
        int fencing = Integer.parseInt(record.get(1));
        Duration swimming = parseTime(record.get(2));
        int knockingDown = Integer.parseInt(record.get(3));
        int refusal = Integer.parseInt(record.get(4));
        int disobedienceLeading = Integer.parseInt(record.get(5));
        int shooting = Integer.parseInt(record.get(6));
        Duration running = parseTime(record.get(7));

        long points = calculateRiddingPoints(knockingDown, refusal, disobedienceLeading)
                + calculateSwimmingPoints(swimming) + calculatingFencingPoints(athletesCount, fencing)
                + calculatingShootingPoints(shooting);

        return new AthleteResults()
                .setFullName(fullName)
                .setFencing(fencing)
                .setShooting(shooting)
                .setSwimming(swimming)
                .setRun(running)
                .setPoints(points);
    }

    private Duration parseTime(String time) {
        String iso = time.replaceFirst("^(\\d+):(\\d+(?:\\.\\d*)?)$", "PT$1M$2S");
        return Duration.parse(iso);
    }

    private int calculatingShootingPoints(int shootingResult) {
        final int TARGET_SCORE = 172;
        int differenceFromTarget = shootingResult - TARGET_SCORE;

        if (shootingResult < TARGET_SCORE) {
            return 1000 - Math.abs(differenceFromTarget) * 12;
        } else {
            return 1000 + differenceFromTarget * 12;
        }
    }

    private int calculatingFencingPoints(int athletesCount, int fencingResult) {
        final int TARGET_SCORE = 70;
        int percentageOfVictories = (fencingResult / (athletesCount - 1)) * 100;
        int diff = fencingResult - (athletesCount);

        if (percentageOfVictories < TARGET_SCORE) {
            return 1000 - Math.abs(diff) * 40;
        } else {
            return 1000 + Math.abs(diff) * 40;
        }
    }

    private int calculateSwimmingPoints(Duration run) {
        Duration targetDuration = Duration.ofMillis(150000);
        long actual = run.toMillis();
        long target = targetDuration.toMillis();
        int difference = (Math.toIntExact(actual) - Math.toIntExact(target)) / 3000;

        if (actual > target) {
            return 1000 - Math.abs(difference) * 4;
        } else {
            return 1000 + Math.abs(difference) * 4;
        }
    }

    private int calculateRiddingPoints(int knockingDown, int refusal, int disobedienceLeading) {
        final int FAULT_FREE_RIDE = 1200;

        return FAULT_FREE_RIDE - (knockingDown * 28 + refusal * 40 + disobedienceLeading * 60);

    }

}
