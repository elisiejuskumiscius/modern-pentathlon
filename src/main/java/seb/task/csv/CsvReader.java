package seb.task.csv;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import seb.task.exceptions.SebResponseException;
import seb.task.model.AthleteResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static seb.task.messages.ErrorMessage.FAIL_COUNTING_LINES;
import static seb.task.messages.ErrorMessage.FAIL_READING_FILE;

@Service
public class CsvReader {

    private static final Logger log = LoggerFactory.getLogger(CsvReader.class);

    private static int countLines() throws SebResponseException {
        BufferedReader bufferedReader;
        int count = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader("Athlete_Results.csv"));
            while (Objects.nonNull(bufferedReader.readLine())) {
                count++;
            }
        } catch (IOException e) {
            var message = String.format("Failed counting lines %s!", e.getMessage());
            log.error(message);
            throw SebResponseException.createValidation(FAIL_COUNTING_LINES, message);
        }
        return count;
    }

    private static int countCommas(String line) {
        int count = 0;
        for (String subString : line.split(",")) {
            count++;
        }
        return count;
    }

    public List<AthleteResults> readCSV() throws SebResponseException {
        Instant start = Instant.now();
        List<AthleteResults> athleteResults = new ArrayList<>();
        File csvFile = new File("Athlete_Results.csv");

        try {
            Files.readAllLines(csvFile.toPath(), StandardCharsets.UTF_8).forEach(line -> {
                var commas = countCommas(line);
                List<String> records = new ArrayList<>(Arrays.asList(line.split(",")));
                while (records.size() < commas) {
                    records.add("");
                }
                try {
                    athleteResults.add(parseAthleteResults(records, countLines()));
                } catch (SebResponseException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            var message = String.format("Failed reading %s file!", e.getMessage());
            log.error(message);
            throw SebResponseException.createValidation(FAIL_READING_FILE, message);
        }

        log.info("Athlete_Results.csv read successfully");
        log.info("Reading took: {}ms", Duration.between(start, Instant.now()).toMillis());
        return athleteResults;
    }

    private AthleteResults parseAthleteResults(List<String> records, int athletesCount) {
        String fullName = records.get(0);
        int fencing = Integer.parseInt(records.get(1));
        Duration swimming = parseTime(records.get(2));
        int knockingDown = Integer.parseInt(records.get(3));
        int refusal = Integer.parseInt(records.get(4));
        int disobedienceLeading = Integer.parseInt(records.get(5));
        int shooting = Integer.parseInt(records.get(6));
        Duration running = parseTime(records.get(7));

        long points = calculateRiddingPoints(knockingDown, refusal, disobedienceLeading)
                + calculateSwimmingPoints(swimming) + calculatingFencingPoints(athletesCount, fencing)
                + (long) calculatingShootingPoints(shooting);

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
