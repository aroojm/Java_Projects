package edu.dev10.solarfarm.models;

import java.util.Objects;

public class Panel {
    private int panelId;
    private String section;
    private int row;
    private int column;
    private MaterialType type;
    private int year;
    private boolean isTracking;

    public int getPanelId() {
        return panelId;
    }

     public void setPanelId(int panelId) {
        this.panelId = panelId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public MaterialType getType() {
        return type;
    }

    public void setType(MaterialType type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public Panel() {
    }

    // id not included in constructor as it is determined by repository
    public Panel(String section, int row, int column, MaterialType type, int year, boolean isTracking) {
        this.section = section;
        this.row = row;
        this.column = column;
        this.type = type;
        this.year = year;
        this.isTracking = isTracking;
    }

    public Panel(int panelId, String section, int row, int column, MaterialType type, int year, boolean isTracking) {
        this.panelId = panelId;
        this.section = section;
        this.row = row;
        this.column = column;
        this.type = type;
        this.year = year;
        this.isTracking = isTracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Panel panel = (Panel) o;
        return panelId == panel.panelId && row == panel.row &&
                column == panel.column && isTracking == panel.isTracking &&
                section.equals(panel.section) && type == panel.type && year == panel.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(panelId, section, row, column, type, year, isTracking);
    }
}
