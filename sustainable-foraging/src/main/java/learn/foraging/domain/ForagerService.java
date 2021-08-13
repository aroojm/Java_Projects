package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForagerService {

    private final ForagerRepository repository;

    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Result<Forager> add(Forager forager) throws DataException {
        Result<Forager> result = validate(forager);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(repository.add(forager));
        return result;
    }

    public Result<Forager> validate(Forager forager) {
        Result<Forager> result = validateNulls(forager);
        if (!result.isSuccess()) {
            return result;
        }
        checkDuplicate(forager, result);
        return result;
    }
    private Result<Forager> validateNulls(Forager forager) {
        Result<Forager> result = new Result<>();
        if (forager == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (forager.getFirstName() == null) {
            result.addErrorMessage("First name is required.");
        }

        if (forager.getLastName() == null) {
            result.addErrorMessage("Last name is required.");
        }

        if (forager.getState() == null) {
            result.addErrorMessage("State is required.");
        }

        if (forager.getFirstName().isBlank() || forager.getLastName().isBlank()) {
            result.addErrorMessage("Forager name cannot be empty.");
        }
        return result;
    }
    private void checkDuplicate(Forager forager, Result<Forager> result) {
        List<Forager> foragers = findByLastName(forager.getLastName());
        for(Forager f : foragers) {
            if (forager.equals(f)) {
                result.addErrorMessage("Duplicate forager not allowed");
                return;
            }
        }
    }
}
