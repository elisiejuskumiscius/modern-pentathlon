package seb.task.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import seb.task.exceptions.ResponseBase;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AthleteResultsResponse extends ResponseBase {
    private List<AthleteResults> athleteResults;
}
