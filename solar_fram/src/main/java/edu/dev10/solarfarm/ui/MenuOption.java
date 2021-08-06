package edu.dev10.solarfarm.ui;

public enum MenuOption {
    EXIT("Exit"),
    DISPLAY_PANELS_BY_SECTION("Find Panels by Section"),
    DISPLAY_PANELS_BY_MATERIAL("Find Panels by Material"),
    ADD_PANEL("Add a Panel"),
    UPDATE_PANEL("Update a Panel"),
    REMOVE_PANEL("Remove a Panel");
    private final String title;

    MenuOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
