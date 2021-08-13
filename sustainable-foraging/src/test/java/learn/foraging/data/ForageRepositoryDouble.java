package learn.foraging.data;

import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ForageRepositoryDouble implements ForageRepository {

    final LocalDate date = LocalDate.of(2020, 6, 26);

    private final ArrayList<Forage> forages = new ArrayList<>();

    public ForageRepositoryDouble() {
        Forage forage = new Forage();
        forage.setId("498604db-b6d6-4599-a503-3d8190fda823");
        forage.setDate(date);
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(1.25);
        forages.add(forage);

// new forages added
        LocalDate date1 = LocalDate.of(2012, 12, 12);

        Forage forage1 = new Forage();
        forage1.setId("b1926469-62f0-4f8a-b72b-f868b11a8ce2");
        forage1.setDate(date1);
        forage1.setForager(ForagerRepositoryDouble.FORAGER);
        forage1.setItem(ItemRepositoryDouble.ITEM);
        forage1.setKilograms(1.25);
        forages.add(forage1);

        Forage forage2 = new Forage();
        forage2.setId("ee6350e9-cb50-4f46-8c7a-28e375c23dd1");
        forage2.setDate(date1);
        forage2.setForager(ForagerRepositoryDouble.FORAGER2);
        forage2.setItem(ItemRepositoryDouble.ITEM);
        forage2.setKilograms(10.0);
        forages.add(forage2);

        Forage forage3 = new Forage();
        forage3.setId("fb301e7b-4c59-47ce-9250-3535aa77edc7");
        forage3.setDate(date1);
        forage3.setForager(ForagerRepositoryDouble.FORAGER);
        forage3.setItem(ItemRepositoryDouble.ITEM2);
        forage3.setKilograms(10.0);
        forages.add(forage3);

        Forage forage4 = new Forage();
        forage4.setId("bc55a815-7174-4f61-82a6-f58528b36851");
        forage4.setDate(date1);
        forage4.setForager(ForagerRepositoryDouble.FORAGER2);
        forage4.setItem(ItemRepositoryDouble.ITEM2);
        forage4.setKilograms(10.0);
        forages.add(forage4);

        Forage forage5 = new Forage();
        forage5.setId("2f7194e4-75c0-4496-a93d-311476eec6a2");
        forage5.setDate(date1);
        forage5.setForager(ForagerRepositoryDouble.FORAGER2);
        forage5.setItem(ItemRepositoryDouble.ITEM3);
        forage5.setKilograms(10.0);
        forages.add(forage5);

        Forage forage6 = new Forage();
        forage6.setId("9fed7e0c-f569-489b-8c9e-64ca3dbd46d1");
        forage6.setDate(date1);
        forage6.setForager(ForagerRepositoryDouble.FORAGER2);
        forage6.setItem(ItemRepositoryDouble.ITEM4);
        forage6.setKilograms(10.0);
        forages.add(forage6);

        Forage forage7 = new Forage();
        forage7.setId("de922e50-1828-41b7-89ed-ef2eefa19b08");
        forage7.setDate(date1);
        forage7.setForager(ForagerRepositoryDouble.FORAGER2);
        forage7.setItem(ItemRepositoryDouble.ITEM5);
        forage7.setKilograms(10.0);
        forages.add(forage7);
    }

    @Override
    public List<Forage> findByDate(LocalDate date) {
        return forages.stream()
                .filter(i -> i.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public Forage add(Forage forage) throws DataException {
        forage.setId(java.util.UUID.randomUUID().toString());
        forages.add(forage);
        return forage;
    }

    @Override
    public boolean update(Forage forage) throws DataException {
        return false;
    }
}
