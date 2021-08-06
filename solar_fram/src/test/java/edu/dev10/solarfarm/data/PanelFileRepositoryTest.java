package edu.dev10.solarfarm.data;

import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PanelFileRepositoryTest {
    private static final String SEED_PATH = "./data/panels-seed.csv";
    private static final String TEST_PATH = "./data/panels-test.csv" ;
    private PanelFileRepository repository = new PanelFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindSevenPanels() throws DataAccessException {
        List<Panel> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(7, actual.size());
    }

    @Test
    void shouldFindExistingPanel() throws DataAccessException {
        Panel actual = repository.findById(3);
        assertNotNull(actual);
        Panel expected = new Panel(3,"Upper Hill", 10, 100, MaterialType.MONO_SI, 2018, true);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindMissingPanel() throws DataAccessException {
        Panel missing = repository.findById(10);
        assertNull(missing);
    }

    @Test
    void shouldFindPanelsBySection() throws DataAccessException {
        List<Panel> flats = repository.findBySection("Flats");
        assertNotNull(flats);
        assertEquals(3, flats.size());

        List<Panel> lowerHill = repository.findBySection("Lower Hill");
        assertNotNull(lowerHill);
        assertEquals(1, lowerHill.size());
    }

    @Test
    void shouldNotFindPanelsInMissingSections() throws DataAccessException {
        List<Panel> missing = repository.findBySection("Does Not Exist");
        assertNotNull(missing);
    }

    @Test
    void shouldFindFiveSections() throws DataAccessException {
        Set<String> actual = repository.findAllSections();
        assertNotNull(actual);
        assertEquals(5, actual.size());
    }
    @Test
    void shouldFindPanelsByYearRange() throws DataAccessException {
        List<Panel> panels = repository.findByYearRanges(2010, 2015);
        assertEquals(4,panels.size());
        panels = repository.findByYearRanges(2016, 2019);
        assertEquals(1,panels.size());
    }
    @Test
    void shouldFindPanelsByMaterialType() throws DataAccessException {
        List<Panel> panels = repository.findByMaterial(MaterialType.AMP_SI);
        assertEquals(3,panels.size());
        panels = repository.findByMaterial(MaterialType.CIGS);
        assertEquals(1,panels.size());
    }

    @Test
    void shouldAddPanel() throws DataAccessException {
        Panel panel = new Panel("Main", 4, 5, MaterialType.CDTD, 2019, true);
        Panel actual = repository.add(panel);
        assertNotNull(actual);
        assertEquals(8, actual.getPanelId());
    }

    @Test
    void shouldUpdateExistingPanel() throws DataAccessException {
        Panel panel = new Panel(2,"Test Section", 111, 222, MaterialType.CIGS, 2011, true);

        boolean success = repository.update(panel);
        assertTrue(success);

        Panel actual = repository.findById(2);
        assertEquals(panel, actual);
    }

    @Test
    void shouldNotUpdateMissingPanel() throws DataAccessException {
        Panel panel = new Panel(1234,"Test Section", 111, 222, MaterialType.CIGS, 2011, true);
        assertFalse(repository.update(panel));
    }

    @Test
    void shouldDeleteExistingPanel() throws DataAccessException {
        assertTrue(repository.delete(3));

        Panel panel = repository.findById(3);
        assertNull(panel);
    }

    @Test
    void shouldNotDeleteMissingPanel() throws DataAccessException {
        assertFalse(repository.delete(1234));

    }
}