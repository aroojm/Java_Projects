package learn.lodging.data;

import learn.lodging.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class HostFileRepository implements HostRepository {

    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "~~~";

    private final String filePath;
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    public HostFileRepository(@Value("${hostDataFilePath}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Host findById(String id) {
        return findAll().stream()
                .filter(i -> i.getHostId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Host> findByLastName(String prefix) {
        return findAll().stream()
                .filter(g -> g.getLastName().toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }


    private String serialize(Host host) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                host.getHostId(),
                clean(host.getLastName()),
                clean(host.getEmail()),
                clean(host.getPhone()),
                clean(host.getAddress()),
                clean(host.getCity()),
                host.getState(),
                clean(host.getPostalCode()),
                host.getStandardRate(),
                host.getWeekendRate());
    }

    private Host deserialize(String[] fields) {
        Host result = new Host();
        result.setHostId(fields[0]);
        result.setLastName(restore(fields[1]));
        result.setEmail(restore(fields[2]));
        result.setPhone(restore(fields[3]));
        result.setAddress(restore(fields[4]));
        result.setCity(restore(fields[5]));
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(new BigDecimal(fields[8]));
        result.setWeekendRate(new BigDecimal(fields[9]));
        return result;
    }

    private void writeAll(List<Host> hosts) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            for (Host host : hosts) {
                writer.println(serialize(host));
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

//    @Override
//    public Host add(Host host) {
//        return null;
//    }
//
//    @Override
//    public boolean update(Host host) {
//        return false;
//    }
//
//    @Override
//    public boolean delete(Host host) {
//        return false;
//    }

}
