package seb.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seb.task.model.AthleteResults;
import seb.task.service.AthletesService;

import java.util.List;

@RestController
public class AthletesController {

    @Autowired
    private AthletesService athletesService;

    @GetMapping("/athletes")
    public List<AthleteResults> getAthletes() {
        return athletesService.getAllAthletes();
    }
}
