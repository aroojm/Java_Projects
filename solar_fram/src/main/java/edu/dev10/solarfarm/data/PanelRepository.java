package edu.dev10.solarfarm.data;

import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;

import java.util.List;
import java.util.Set;

public interface PanelRepository {

    List<Panel> findAll() throws DataAccessException;

    Panel findById(int panelId) throws DataAccessException;

    List<Panel> findBySection(String section) throws DataAccessException;

    Set<String> findAllSections() throws DataAccessException;

    List<Panel> findByYearRanges(int min, int max) throws DataAccessException;

    List<Panel> findByMaterial(MaterialType materialType) throws DataAccessException;

    Panel add(Panel panel) throws DataAccessException;

    boolean update(Panel panel) throws DataAccessException;

    boolean delete(int panelId) throws DataAccessException;

}
