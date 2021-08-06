package edu.dev10.solarfarm.domain;

import edu.dev10.solarfarm.data.DataAccessException;
import edu.dev10.solarfarm.data.PanelRepository;
import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PanelService {
    private final PanelRepository repository;

    public PanelService(PanelRepository repository) {
        this.repository = repository;
    }

    public List<Panel> findBySection(String section) throws DataAccessException {
        return repository.findBySection(section);
    }
    public Set<String> findAllSections() throws DataAccessException {
        return repository.findAllSections();
    }
    public List<Panel> findByYearRanges(int min, int max) throws DataAccessException {
        return repository.findByYearRanges(min, max);
    }
    public List<Panel> findByMaterial(MaterialType type) throws DataAccessException {
        return repository.findByMaterial(type);
    }

    public PanelResult add(Panel panel) throws DataAccessException {
        PanelResult result = validateBeforeAdding(panel);
        if (!result.isSuccess()) {
            return result;
        }
        Panel module = repository.add(panel);
        result.setModule(module);
        return result;
    }
    public PanelResult update(Panel panel) throws DataAccessException {
        PanelResult result = validateBeforeUpdating(panel);
        if (!result.isSuccess()) {
            return result;
        }
        repository.update(panel);
        result.setModule(panel);
        return result;
    }
    public PanelResult delete(int panelId) throws DataAccessException {
        PanelResult result = new PanelResult();
        Panel existing = repository.findById(panelId);
        if (existing == null) {
            result.addErrorMessage(String.format("Panel Id %d not found.", panelId));
            return result;
        }
        repository.delete(existing.getPanelId());
        result.setModule(existing);
        return result;
    }


    /**
     * Returns id of an occupied spot if panel at spot is different than input panel
     * otherwise returns id of input panel
     * @param panel
     * @return
     * @throws DataAccessException
     */
    private int idDuplicateLocation(Panel panel) throws DataAccessException {
        List<Panel> allPanels = repository.findAll();
        for (Panel p : allPanels) {
            if (p.getSection().equals(panel.getSection()) &&
                    p.getRow() == panel.getRow() &&
                    p.getColumn() == panel.getColumn()) {
                return p.getPanelId();
            }
        }
        return panel.getPanelId();
    }
    private PanelResult validateBeforeUpdating(Panel panel) throws DataAccessException {
        PanelResult result = validateInputs(panel);
        if (!result.isSuccess()) {
            return result;
        }
        Panel existing = repository.findById(panel.getPanelId());
        if (existing == null) {
            result.addErrorMessage(String.format("Panel Id %d not found.",panel.getPanelId()));
            return result;
        }
        if (idDuplicateLocation(panel) != panel.getPanelId()) {
            result.addErrorMessage(String.format("Another panel already exists at %s-%d-%d. Cannot update to this location.",
                    panel.getSection(), panel.getRow(), panel.getColumn()));
        }
        return result;
    }
    private PanelResult validateBeforeAdding(Panel panel) throws DataAccessException {
        PanelResult result = validateInputs(panel);
        if (!result.isSuccess()) {
            return result;
        }
        if (panel.getPanelId() > 0) {
            result.addErrorMessage("Panel Id should not be set.");
            return result;
        }
        // panelId is 0 as no id assigned by repository yet
        if (idDuplicateLocation(panel) != panel.getPanelId()) {
            result.addErrorMessage(String.format("Another panel already exists at %s-%d-%d. Cannot add at this location.",
                    panel.getSection(), panel.getRow(), panel.getColumn()));
        }
        return result;
    }
    private PanelResult validateInputs(Panel panel) {
        PanelResult result = new PanelResult();

        if (panel == null) {
            result.addErrorMessage("Panel cannot be null.");
            return result;
        }
        if (panel.getSection() == null || panel.getSection().isBlank()) {
            result.addErrorMessage("Section is required.");
        }
        if (panel.getRow() < 1 || panel.getRow() > 250) {
            result.addErrorMessage("Valid row number: 1 to 250");
        }
        if (panel.getColumn() < 1 || panel.getColumn() > 250) {
            result.addErrorMessage("Valid column number: 1 to 250");
        }
        if (panel.getYear() > 2021) {
            result.addErrorMessage("Installation year cannot be in future.");
        }
        if (panel.getYear() < 1950) {
            result.addErrorMessage("Solar Technology did not exist at that time.");
        }
        if (panel.getType() == null) {
            result.addErrorMessage("Material is required.");
        }
        return result;
    }


}
