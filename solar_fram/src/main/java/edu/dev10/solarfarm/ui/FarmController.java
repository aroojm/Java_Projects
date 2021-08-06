package edu.dev10.solarfarm.ui;

import edu.dev10.solarfarm.data.DataAccessException;
import edu.dev10.solarfarm.domain.PanelResult;
import edu.dev10.solarfarm.domain.PanelService;
import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class FarmController {
    private final PanelService service;
    private final View view;

    public FarmController(PanelService service, View view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        try {
            view.printHeader("Welcome to Solar Farm");
            runMenu();
        } catch (DataAccessException e) {
            view.printHeader("Fatal Error: " + e.getMessage());
        }
    }
    private void runMenu() throws DataAccessException {
        MenuOption option;
        do {
            option = view.displayMenuAndSelect();
            switch (option) {
                case EXIT:
                    view.printHeader("Goodbye!");
                    break;
                case DISPLAY_PANELS_BY_SECTION:
                    displayPanelsBySections();
                    break;
                case DISPLAY_PANELS_BY_MATERIAL:
                    displayPanelsByMaterials();
                    break;
                case ADD_PANEL:
                    addPanel();
                    break;
                case UPDATE_PANEL:
                    updatePanel();
                    break;
                case REMOVE_PANEL:
                    removePanel();
                    break;
            }
        } while(option != MenuOption.EXIT);
    }
    public void displayPanelsBySections() throws DataAccessException {
        view.printHeader(MenuOption.DISPLAY_PANELS_BY_SECTION.getTitle());
        List<Panel> panels = getPanelsInSection("Choose section to view:");
        if (panels == null) { return; }
        view.printPanelsInSection(panels);
    }
    public void displayPanelsByMaterials() throws DataAccessException {
        view.printHeader(MenuOption.DISPLAY_PANELS_BY_MATERIAL.getTitle());
        MaterialType material = view.displayMaterialsAndSelect();
        List<Panel> panels = service.findByMaterial(material);
        view.printPanelsOfMaterialType(panels);
    }
    public void addPanel() throws DataAccessException {
        view.printHeader(MenuOption.ADD_PANEL.getTitle());
        Panel panel = view.makePanel();
        PanelResult result = service.add(panel);
        view.printResult(result, "added");
    }
    public void updatePanel() throws DataAccessException {
        view.printHeader(MenuOption.UPDATE_PANEL.getTitle());
        List<Panel> panels = getPanelsInSection("Choose section to update from: ");
        if (panels == null) { return; }
        Panel panel = view.update(panels);
        if (panel != null) {
            PanelResult result = service.update(panel);
            view.printResult(result, "updated");
        }
    }
    public void removePanel() throws DataAccessException {
        view.printHeader(MenuOption.REMOVE_PANEL.getTitle());
        List<Panel> panels = getPanelsInSection("Choose section to delete from: ");
        if (panels == null) { return; }
        Panel panel = view.findPanel(panels);
        if (panel != null) {
            PanelResult result = service.delete(panel.getPanelId());
            view.printResult(result, "deleted");
        }
    }
    private List<Panel> getPanelsInSection(String input) throws DataAccessException {
        Set<String> sections = service.findAllSections();
        String section = view.displaySectionsAndSelect(sections, input);
        if (!section.isEmpty()) {
            return service.findBySection(section);
        }
        return null;
    }

}
