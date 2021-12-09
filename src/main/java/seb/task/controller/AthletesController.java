package seb.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seb.task.exceptions.ResponseUtils;
import seb.task.model.AthleteResultsResponse;
import seb.task.service.AthletesService;

@RestController
public class AthletesController {

    @Autowired
    private AthletesService athletesService;

    @GetMapping("/athletes")
    public AthleteResultsResponse getAthletesResponse() {
        AthleteResultsResponse resultsResponse = new AthleteResultsResponse();
        ResponseUtils.handleResponseMessageFormatting(
                resultsResponse::setStatus,
                resultsResponse::setMessages,
                () -> resultsResponse.setAthleteResults(athletesService.getAllAthletes()));
        return resultsResponse;
    }
}
