package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;

    private final DeveloperTax developerTax;

    @Autowired
    public DeveloperController( DeveloperTax developerTax) {
        this.developerTax = developerTax;
    }

    @PostConstruct
    public void init() {
       developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }


    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public void addDeveloper(@RequestBody Developer developerRequest) {
        Developer developer;
        switch (developerRequest.getExperience()) {
            case JUNIOR:
                developer = new JuniorDeveloper(developerRequest.getId(), developerRequest.getName(), developerRequest.getSalary() - developerRequest.getSalary() * developerTax.getSimpleTaxRate() / 100);
                break;
            case MID:
                developer = new MidDeveloper(developerRequest.getId(), developerRequest.getName(), developerRequest.getSalary() - developerRequest.getSalary() * developerTax.getMiddleTaxRate() / 100);
                break;
            case SENIOR:
                developer = new SeniorDeveloper(developerRequest.getId(), developerRequest.getName(), developerRequest.getSalary() - developerRequest.getSalary() * developerTax.getUpperTaxRate() / 100);
                break;
            default:
                throw new IllegalArgumentException("Invalid experience level");
        }
        developers.put(developerRequest.getId(), developer);
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer developerRequest) {
        developers.put(id, developerRequest);
        return developerRequest;
    }

    @DeleteMapping("/{id}")
    public String deleteDeveloper(@PathVariable int id) {
        Developer removedDeveloper = developers.remove(id);
        return removedDeveloper != null ? "Developer with ID " + id + " deleted" : "Developer not found";
    }
}

