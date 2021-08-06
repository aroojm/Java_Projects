package edu.dev10.solarfarm.data;

import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class PanelFileRepository implements PanelRepository {
    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";
    private static final String HEADER = "panelId,section,row,column,type,year,isTracking";
    private final String filePath;

    public PanelFileRepository(@Value("${dataFilePath}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Panel findById(int panelId) throws DataAccessException {
        List<Panel> allPanels = findAll();
        for (Panel panel : allPanels) {
            if (panel.getPanelId() == panelId) {
                return panel;
            }
        }
        return null;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataAccessException {
        List<Panel> allPanels = findAll();
        List<Panel> panels = new ArrayList<>();
        for (Panel panel : allPanels) {
            if (panel.getSection().equals(section)) {
                panels.add(panel);
            }
        }
        return panels;
    }

    @Override
    public Set<String> findAllSections() throws DataAccessException {
        List<Panel> allPanels = findAll();
        Set<String> panels = new HashSet<>();
        for (Panel panel : allPanels) {
            panels.add(panel.getSection());
        }
        return panels;
    }

    @Override
    public List<Panel> findByYearRanges(int min, int max) throws DataAccessException {
        List<Panel> allPanels = findAll();
        List<Panel> panels = new ArrayList<>();
        for (Panel panel : allPanels) {
            if (panel.getYear() >= min && panel.getYear() <= max) {
                panels.add(panel);
            }
        }
        return panels;
    }

    @Override
    public List<Panel> findByMaterial(MaterialType type) throws DataAccessException {
        List<Panel> allPanels = findAll();
        List<Panel> panels = new ArrayList<>();
        for (Panel panel : allPanels) {
            if (panel.getType() == type) {
                panels.add(panel);
            }
        }
        return panels;
    }


    @Override
    public Panel add(Panel panel) throws DataAccessException {
        List<Panel> allPanels = findAll();
        panel.setPanelId(getNextId(allPanels));
        allPanels.add(panel);
        writeAll(allPanels);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        List<Panel> allPanels = findAll();
        for (int i = 0; i < allPanels.size(); i++) {
            if (allPanels.get(i).getPanelId() == panel.getPanelId()) {
                allPanels.set(i, panel);
                writeAll(allPanels);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int panelId) throws DataAccessException {
        List<Panel> allPanels = findAll();
        for (int i = 0; i < allPanels.size(); i++) {
            if (allPanels.get(i).getPanelId() == panelId) {
                allPanels.remove(allPanels.get(i));
                writeAll(allPanels);
                return true;
            }
        }
        return false;
    }

    // file to list method
    @Override
    public List<Panel> findAll() throws DataAccessException {
        ArrayList<Panel> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Panel panel = deserialize(line);
                if (panel != null) {
                    result.add(panel);
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return result;
    }

    // list to file method
    private void writeAll(List<Panel> panels) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Panel panel : panels) {
                writer.println(serialize(panel));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private int getNextId(List<Panel> panels) {
        int nextId = 0;
        for (Panel panel : panels) {
            nextId = Math.max(nextId, panel.getPanelId());
        }
        return nextId + 1;
    }

    private String serialize(Panel panel) {
        return String.format("%d,%s,%d,%d,%s,%s,%s",
                panel.getPanelId(),
                clean(panel.getSection()),
                panel.getRow(),
                panel.getColumn(),
                panel.getType(),
                panel.getYear(),
                panel.isTracking());
    }

    private Panel deserialize(String line) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length == 7) {
            Panel panel = new Panel();
            panel.setPanelId(Integer.parseInt(fields[0]));
            panel.setSection(restore(fields[1]));
            panel.setRow(Integer.parseInt(fields[2]));
            panel.setColumn(Integer.parseInt(fields[3]));
            panel.setType(MaterialType.valueOf(fields[4]));
            panel.setYear(Integer.parseInt(fields[5]));
            panel.setTracking("true".equals(fields[6]));
            return panel;
        }
        return null;
    }

    private String clean(String value) {
        return value.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private String restore(String value) {
        return value.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }

}
