package learn.lodging.data;

import learn.lodging.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
@Repository
public class GuestFileRepository implements GuestRepository {

    private final String filePath;
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "~~~";

    public GuestFileRepository(@Value("${guestDataFilePath}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Guest findById(int id) {
        return findAll().stream()
                .filter(i -> i.getGuestId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Guest> findByLastName(String prefix) {
        return findAll().stream()
                .filter(g -> g.getLastName().toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    private String serialize(Guest guest) {
        //guest_id,first_name,last_name,email,phone,state
        return String.format("%s,%s,%s,%s,%s,%s",
                guest.getGuestId(),
                clean(guest.getFirstName()),
                clean(guest.getLastName()),
                clean(guest.getEmail()),
                clean(guest.getPhone()),
                guest.getState());
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestId(Integer.parseInt(fields[0]));
        result.setFirstName(restore(fields[1]));
        result.setLastName(restore(fields[2]));
        result.setEmail(restore(fields[3]));
        result.setPhone(restore(fields[4]));
        result.setState(fields[5]);
        return result;
    }

    private void writeAll(List<Guest> guests) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

//            if (guests == null) {
//                return;
//            }

            for (Guest guest : guests) {
                writer.println(serialize(guest));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }


    private String clean(String value) {
        return value.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private String restore(String value) {
        return value.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }



}
