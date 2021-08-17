package learn.lodging.data;

import learn.lodging.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class HostFileRepositoryTest {
    private static final String SEED_PATH = "./data/hosts-seed.csv";
    private static final String TEST_PATH = "./data/hosts-test.csv" ;
    private HostFileRepository repository = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Host> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldFindByLastName() {
        List<Host> hosts = repository.findByLastName("a");
        assertNotNull(hosts);
        assertEquals(28, hosts.size());

        hosts = repository.findByLastName("WOR");
        assertNotNull(hosts);
        assertEquals(3, hosts.size());

        hosts = repository.findByLastName("Zuppa");
        assertNotNull(hosts);
        assertEquals("d63304e3-de36-4ecc-8f8f-847431ffff64", hosts.get(0).getHostId());
    }

    @Test
    void shouldFindById() {
        Host host = repository.findById("98de64b6-b05b-4d68-b936-4efc1771f413");
        assertNotNull(host);
        assertEquals("98de64b6-b05b-4d68-b936-4efc1771f413", host.getHostId());
    }

}