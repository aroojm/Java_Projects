package learn.lodging.data;

import learn.lodging.models.Guest;

import java.util.ArrayList;
import java.util.List;


public class GuestRepositoryDouble implements GuestRepository{
    private final ArrayList<Guest> guests = new ArrayList<>();

    public final static Guest GUEST1 = new Guest(1,"FirstGuest","Test","test1@mediafire.com","(702) 7768761","NV");
    public final static Guest GUEST2 = new Guest(2,"SecondGuest","Test","test2@dagondesign.com","(202) 2528316","DC");
    public final static Guest GUEST3 = new Guest(3,"ThirdGuest","Test","test3@japanpost.jp","(313) 2245034","MI");


    public GuestRepositoryDouble() {
        guests.add(GUEST1);
        guests.add(GUEST2);
        guests.add(GUEST3);
    }

    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest findById(int id) {
        return guests.stream()
                .filter(g -> g.getGuestId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Guest> findByLastName(String lastName) {
        return new ArrayList<>(guests);
    }
}
