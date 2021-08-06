package edu.dev10.solarfarm.data;

import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PanelRepositoryDouble implements  PanelRepository{
    private ArrayList<Panel> panels = new ArrayList<>(); // contains dummy data

    public PanelRepositoryDouble() {
        panels.add(new Panel(1,"Flats", 1, 1, MaterialType.CDTD, 2012, false));
        panels.add(new Panel(2,"Flats", 2, 2, MaterialType.CIGS, 2013, false));
        panels.add(new Panel(3,"Hills", 3, 3, MaterialType.MULTI_SI, 2014, true));
        panels.add(new Panel(4,"Trees", 4, 4, MaterialType.MONO_SI, 2015, true));
    }

    @Override
    public List<Panel> findAll() throws DataAccessException {
        return new ArrayList<>(panels);
    }

    @Override
    public Panel findById(int panelId) throws DataAccessException {
        for (Panel panel : panels) {
            if (panel.getPanelId() == panelId) {
                return panel;
            }
        }
        return null;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataAccessException {
        ArrayList<Panel> result = new ArrayList<>();
        for (Panel panel : panels) {
            if (panel.getSection().equals(section)) {
                result.add(panel);
            }
        }
        return result;
    }

    @Override
    public Set<String> findAllSections() throws DataAccessException {
        Set<String> sections = new HashSet<>();
        for (Panel panel : panels) {
            sections.add(panel.getSection());
        }
        return sections;
    }

    @Override
    public List<Panel> findByYearRanges(int min, int max) throws DataAccessException {
       return new ArrayList<>(panels);
    }

    @Override
    public List<Panel> findByMaterial(MaterialType type) throws DataAccessException {
        ArrayList<Panel> result = new ArrayList<>();
        result.add(panels.get(1));
        return result;
    }

    @Override
    public Panel add(Panel panel) throws DataAccessException {
        panels.add(panel);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        return true;
    }

    @Override
    public boolean delete(int panelId) throws DataAccessException {
        return true;
    }
}
