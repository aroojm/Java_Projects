package learn.foraging.data;

import learn.foraging.models.Forager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForagerRepositoryDouble implements ForagerRepository {

    public final static Forager FORAGER = makeForager();

    public final static Forager FORAGER2 = makeForager2();

    private final ArrayList<Forager> foragers = new ArrayList<>();

    public ForagerRepositoryDouble() {
        foragers.add(FORAGER);
        foragers.add(FORAGER2);

    }

    @Override
    public Forager findById(String id) {
        return foragers.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Forager> findAll() {
        return foragers;
    }

    @Override
    public List<Forager> findByState(String stateAbbr) {
        return foragers.stream()
                .filter(i -> i.getState().equalsIgnoreCase(stateAbbr))
                .collect(Collectors.toList());
    }

    @Override
    public Forager add(Forager forager) throws DataException {
        return forager;
    }

    private static Forager makeForager() {
        Forager forager = new Forager();
        forager.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");
        forager.setFirstName("Jilly");
        forager.setLastName("Sisse");
        forager.setState("GA");
        return forager;
    }

    private static Forager makeForager2() {
        Forager forager = new Forager();
        forager.setId("826c1b9c-88d0-4f94-950a-ac20740eb2e6");
        forager.setFirstName("TestFirst2");
        forager.setLastName("TestLast2");
        forager.setState("NM");
        return forager;
    }

}
