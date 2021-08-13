package learn.foraging.data;

import learn.foraging.models.Category;
import learn.foraging.models.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryDouble implements ItemRepository {

    public final static Item ITEM = new Item(1, "Chanterelle", Category.EDIBLE, new BigDecimal("9.99"));
    private final ArrayList<Item> items = new ArrayList<>();

    public final static Item ITEM2 = new Item(2, "Jewelweed", Category.MEDICINAL, new BigDecimal("1.65"));
    public final static Item ITEM3 = new Item(3, "Sweet Gum", Category.INEDIBLE, new BigDecimal("5.0"));
    public final static Item ITEM4 = new Item(4, "Jet Berry", Category.POISONOUS, new BigDecimal("2.0"));
    public final static Item ITEM5 = new Item(5, "Dogbane", Category.POISONOUS, new BigDecimal("2.0"));


    public ItemRepositoryDouble() {
        items.add(ITEM);
        items.add(ITEM2);
        items.add(ITEM3);
        items.add(ITEM4);
        items.add(ITEM5);

    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public Item findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Item add(Item item) throws DataException {
        List<Item> all = findAll();

        int nextId = all.stream()
                .mapToInt(Item::getId)
                .max()
                .orElse(0) + 1;

        item.setId(nextId);

        all.add(item);
        return item;
    }
}
