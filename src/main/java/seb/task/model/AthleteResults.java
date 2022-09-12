package seb.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import seb.task.common.Places;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AthleteResults {

    private String fullName;
    private int fencing;
    private Duration swimming;
    private int ridding;
    private int shooting;
    private Duration run;
    private long points;
    private Duration concludingEventTime;
    private String finalTime;
    private Places finalPlace;

}
