package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepositoryDouble;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ForageServiceTest {

    ForageService service = new ForageService(
            new ForageRepositoryDouble(),
            new ForagerRepositoryDouble(),
            new ItemRepositoryDouble());

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(36, result.getPayload().getId().length());
    }

    @Test
    void shouldNotAddWhenForagerNotFound() throws DataException {

        Forager forager = new Forager();
        forager.setId("30816379-188d-4552-913f-9a48405e8c08");
        forager.setFirstName("Ermengarde");
        forager.setLastName("Sansom");
        forager.setState("NM");

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(forager);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenItemNotFound() throws DataException {

        Item item = new Item(11, "Dandelion", Category.EDIBLE, new BigDecimal("0.05"));

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(item);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddDuplicateForage() throws DataException {
        // creating duplicate forage already present in ForageRepositoryDouble
        LocalDate date = LocalDate.of(2020, 6, 26);
        Forage forage = new Forage();

        forage.setDate(date);
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(1.25);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
    }

    @Test
    void shouldCreateCorrectKgReport() throws DataException {
        LocalDate date = LocalDate.of(2012, 12, 12);
        Map<Item, Double> map = service.getDayKgReport(date);
        assertEquals(5, map.keySet().size());
        assertEquals(11.25, map.get(ItemRepositoryDouble.ITEM));
        assertEquals(20, map.get(ItemRepositoryDouble.ITEM2));
        assertEquals(10, map.get(ItemRepositoryDouble.ITEM3));
        assertEquals(10, map.get(ItemRepositoryDouble.ITEM4));
        assertEquals(10, map.get(ItemRepositoryDouble.ITEM5));
    }

    @Test
    void shouldCreateCorrectValueByCategoryReport() throws DataException {
        LocalDate date = LocalDate.of(2012, 12, 12);
        Map<Category, BigDecimal> map = service.getDayValueByCategoryReport(date);
        assertEquals(4, map.keySet().size());
        assertEquals(new BigDecimal(112.3875).setScale(2, RoundingMode.HALF_UP), map.get(Category.EDIBLE).setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal(33).setScale(2), map.get(Category.MEDICINAL).setScale(2));
        assertEquals(new BigDecimal(50).setScale(2), map.get(Category.INEDIBLE).setScale(2));
        assertEquals(new BigDecimal(40).setScale(2), map.get(Category.POISONOUS).setScale(2));
    }


}