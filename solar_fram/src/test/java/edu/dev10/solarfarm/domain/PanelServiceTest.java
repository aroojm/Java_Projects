package edu.dev10.solarfarm.domain;

import edu.dev10.solarfarm.data.DataAccessException;
import edu.dev10.solarfarm.data.PanelRepositoryDouble;
import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelServiceTest {

    PanelService service = new PanelService(new PanelRepositoryDouble());

    @Test
    void shouldFindBySection() throws DataAccessException {
        List<Panel> panels = service.findBySection("Flats");
        assertNotNull(panels);
        assertEquals(2, panels.size());
    }

    @Test
    void shouldNotAddNullPanel() throws DataAccessException {
        assertFalse(service.add(null).isSuccess());
    }
    @Test
    void shouldNotAddPanelIfIdGreaterThan0() throws DataAccessException {
        PanelResult result = service.add(new Panel(10, "Trees", 10, 10, MaterialType.MONO_SI, 2017, false));
        assertNull(result.getModule());
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddPanelAtOccupiedLocation() throws DataAccessException {
        assertFalse(service.add(new Panel("Hills", 3, 3, MaterialType.CIGS, 2016, false)).isSuccess());
    }

    @Test
    void shouldNotAddPanelAtIncorrectLocation() throws DataAccessException {
        assertFalse(service.add(new Panel("Test Section", 251, 1, MaterialType.CIGS, 2016, false)).isSuccess());
        assertFalse(service.add(new Panel("Test Section", 1, 251, MaterialType.CIGS, 2016, false)).isSuccess());
    }

    @Test
    void shouldNotAddPanelWithFutureDate() throws DataAccessException {
        assertFalse(service.add(new Panel("Test Section", 1, 1, MaterialType.CIGS, 2022, false)).isSuccess());
    }
    @Test
    void shouldNotAddPanelAtDateWhenTechDidNotExist() throws DataAccessException {
        assertFalse(service.add(new Panel("Test Section", 1, 1, MaterialType.CIGS, 1940, false)).isSuccess());
    }

    @Test
    void shouldNotAddPanelWithMissingSection() throws DataAccessException {
        assertFalse(service.add(new Panel("", 1, 1, MaterialType.CIGS, 2020, true)).isSuccess());
        assertFalse(service.add(new Panel("     ", 1, 1, MaterialType.CIGS, 2020, true)).isSuccess());
        assertFalse(service.add(new Panel(null , 1, 1, MaterialType.CIGS, 2020, true)).isSuccess());
    }

    @Test
    void shouldNotAddPanelWithMissingMaterial() throws DataAccessException {
        assertFalse(service.add(new Panel("Test Section", 1, 1, null, 2020, false)).isSuccess());
    }

    @Test
    void shouldBeAbleToAddPanel() throws DataAccessException {
        Panel panel = new Panel("Test Section", 100, 100, MaterialType.CIGS, 2019, true);
        PanelResult result = service.add(panel);
        assertTrue(result.isSuccess());
        assertNotNull(result.getModule());
        assertEquals(panel, result.getModule());
    }

    @Test
    void shouldUpdateAtSameLocation() throws DataAccessException {
        Panel panel = new Panel(4,"Trees", 4, 4, MaterialType.MONO_SI, 2020, false);
        assertTrue(service.update(panel).isSuccess());
    }

    @Test
    void shouldUpdateAtEmptyDifferentLocation() throws DataAccessException {
        Panel panel = new Panel(4,"Flats", 5, 5, MaterialType.AMP_SI, 2020, false);
        assertTrue(service.update(panel).isSuccess());
    }
    @Test
    void shouldNotUpdateNonExistingPanel() throws DataAccessException{
        Panel panel = new Panel(100,"Test Section", 5, 5, MaterialType.AMP_SI, 2020, false);
        assertFalse(service.update(panel).isSuccess());
    }

    @Test
    void shouldNotUpdateAtOccupiedLocation() throws DataAccessException {
        Panel panel = new Panel(2,"Trees", 4, 4, MaterialType.MONO_SI, 2015, true);
        assertFalse(service.update(panel).isSuccess());
    }

    @Test
    void shouldDeleteExistingPanel() throws DataAccessException {
        Panel panel = new Panel(1,"Flats", 1, 1, MaterialType.CDTD, 2012, false);
        assertTrue(service.delete(panel.getPanelId()).isSuccess());

    }

    @Test
    void shouldNotDeleteNonExistingPanel() throws DataAccessException {
        Panel panel = new Panel(100,"Test Section", 5, 5, MaterialType.AMP_SI, 2020, false);
        assertFalse(service.delete(panel.getPanelId()).isSuccess());

    }

}