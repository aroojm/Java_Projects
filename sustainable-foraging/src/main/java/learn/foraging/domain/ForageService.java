package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepository;
import learn.foraging.data.ForagerRepository;
import learn.foraging.data.ItemRepository;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForageService {

    private final ForageRepository forageRepository;
    private final ForagerRepository foragerRepository;
    private final ItemRepository itemRepository;

    public ForageService(ForageRepository forageRepository, ForagerRepository foragerRepository, ItemRepository itemRepository) {
        this.forageRepository = forageRepository;
        this.foragerRepository = foragerRepository;
        this.itemRepository = itemRepository;
    }

    public List<Forage> findByDate(LocalDate date) {

        Map<String, Forager> foragerMap = foragerRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));
        Map<Integer, Item> itemMap = itemRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        List<Forage> result = forageRepository.findByDate(date);
        for (Forage forage : result) {
            forage.setForager(foragerMap.get(forage.getForager().getId()));
            forage.setItem(itemMap.get(forage.getItem().getId()));
        }

        return result;
    }

    public Result<Forage> add(Forage forage) throws DataException {
        Result<Forage> result = validate(forage);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(forageRepository.add(forage));

        return result;
    }

    public Map<Item, Double> getDayKgReport(LocalDate date) {
       return findByDate(date).stream()
                .collect(Collectors.groupingBy(Forage::getItem, Collectors.summingDouble(Forage::getKilograms)));
    }

    public Map<Category, BigDecimal> getDayValueByCategoryReport(LocalDate date) {
        List<Forage> forages = findByDate(date);

//        forages.stream().collect(Collectors.groupingBy(f -> f.getItem().getCategory()))
//                .entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().stream()
//                        .map((Forage::getValue).reduce((total, each) -> total = total.add(each)))
//                        .orElse(BigDecimal.ZERO)));

        // using Big Decimal for all calculations
        BigDecimal totalEdible = BigDecimal.ZERO;
        BigDecimal totalMedicinal = BigDecimal.ZERO;
        BigDecimal totalInedible = BigDecimal.ZERO;
        BigDecimal totalPoisonous = BigDecimal.ZERO;
        BigDecimal kg, rate;

        for (Forage forage : forages) {
            kg = BigDecimal.valueOf(forage.getKilograms());
            rate = forage.getItem().getDollarPerKilogram();
            switch (forage.getItem().getCategory()) {
                case EDIBLE:
                    totalEdible = totalEdible.add(kg.multiply(rate));
                    break;
                case MEDICINAL:
                    totalMedicinal = totalMedicinal.add(kg.multiply(rate));
                    break;
                case INEDIBLE:
                    totalInedible = totalInedible.add(kg.multiply(rate));
                    break;
                case POISONOUS:
                    totalPoisonous= totalPoisonous.add(kg.multiply(rate));
                    break;
            }
        }
        Map<Category, BigDecimal> map = new HashMap<>();
        map.put(Category.EDIBLE, totalEdible);
        map.put(Category.MEDICINAL, totalMedicinal);
        map.put(Category.INEDIBLE, totalInedible);
        map.put(Category.POISONOUS, totalPoisonous);

        return map;


        // with streams but converts Big Decimal values to double during intermediate steps
        /**
         forages.stream()
         .collect(Collectors.groupingBy(f -> f.getItem().getCategory(),
         Collectors.summingDouble(f -> f.getKilograms() * f.getItem().getDollarPerKilogram().doubleValue())))
         .forEach((k, v) -> System.out.printf("%s: %.2f\n", k, v));
         */


    }

    public int generate(LocalDate start, LocalDate end, int count) throws DataException {

        if (start == null || end == null || start.isAfter(end) || count <= 0) {
            return 0;
        }

        count = Math.min(count, 500);

        ArrayList<LocalDate> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start);
            start = start.plusDays(1);
        }

        List<Item> items = itemRepository.findAll();
        List<Forager> foragers = foragerRepository.findAll();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Forage forage = new Forage();
            forage.setDate(dates.get(random.nextInt(dates.size())));
            forage.setForager(foragers.get(random.nextInt(foragers.size())));
            forage.setItem(items.get(random.nextInt(items.size())));
            forage.setKilograms(random.nextDouble() * 5.0 + 0.1);
            forageRepository.add(forage);
        }

        return count;
    }

    private Result<Forage> validate(Forage forage) {

        Result<Forage> result = validateNulls(forage);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(forage, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(forage, result);
        if (!result.isSuccess()) {
            return result;
        }

        checkDuplicate(forage, result);

        return result;
    }

    private Result<Forage> validateNulls(Forage forage) {
        Result<Forage> result = new Result<>();

        if (forage == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (forage.getDate() == null) {
            result.addErrorMessage("Forage date is required.");
        }

        if (forage.getForager() == null) {
            result.addErrorMessage("Forager is required.");
        }

        if (forage.getItem() == null) {
            result.addErrorMessage("Item is required.");
        }
        return result;
    }

    private void validateFields(Forage forage, Result<Forage> result) {
        // No future dates.
        if (forage.getDate().isAfter(LocalDate.now())) {
            result.addErrorMessage("Forage date cannot be in the future.");
        }

        if (forage.getKilograms() <= 0 || forage.getKilograms() > 250.0) {
            result.addErrorMessage("Kilograms must be a positive number less than 250.0");
        }
    }

    private void validateChildrenExist(Forage forage, Result<Forage> result) {

        if (forage.getForager().getId() == null
                || foragerRepository.findById(forage.getForager().getId()) == null) {
            result.addErrorMessage("Forager does not exist.");
        }

        if (itemRepository.findById(forage.getItem().getId()) == null) {
            result.addErrorMessage("Item does not exist.");
        }
    }
    private void checkDuplicate(Forage forage, Result<Forage> result) {
        List<Forage> forages = findByDate(forage.getDate());
        for(Forage f : forages) {
            if (forage.getForager().equals(f.getForager()) && forage.getItem().equals(f.getItem())) {
                result.addErrorMessage("Duplicate forage not allowed");
                return;
            }
        }
    }
}
