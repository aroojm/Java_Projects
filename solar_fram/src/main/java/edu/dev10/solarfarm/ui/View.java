package edu.dev10.solarfarm.ui;

import edu.dev10.solarfarm.domain.PanelResult;
import edu.dev10.solarfarm.models.MaterialType;
import edu.dev10.solarfarm.models.Panel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class View {
    private final ConsoleIO console;

    public View(ConsoleIO console) {
        this.console = console;
    }

    public MenuOption displayMenuAndSelect() {
        MenuOption[] values = MenuOption.values();
        printHeader("Main Menu");
        for (int i = 0; i < MenuOption.values().length; i++) {
            System.out.printf("%s. %s \n",i + 1, values[i].getTitle());
        }
        int index = console.readInt("Select [1-6]: ", 1, 6, true);
        return values[index - 1];
    }
    public String displaySectionsAndSelect(Set<String> sections, String heading) {
        if (sections.size() == 0) {
            System.out.println("No Sections to show");
            printSubHeader("No Panels installed");
            return "";
        } else {
            printSubHeader(heading);
            List<String> sectionsList = new ArrayList<>(sections);
            Collections.sort(sectionsList);
            for (int i = 0; i < sectionsList.size(); i++) {
                System.out.printf("%s. %s \n", i + 1, sectionsList.get(i));
            }
            int index = console.readInt(String.format("Select [%d-%d]: ",1, sections.size()), 1, sections.size(), true);
            return sectionsList.get(index - 1);
        }
    }
    public MaterialType displayMaterialsAndSelect() {
         return console.readMaterialType(true);
    }
    public void printPanelsInSection(List<Panel> panels) {
        String section = panels.get(0).getSection();
        printSubHeader(String.format("\nSection Name: %s", section));
        System.out.printf("Panels in \"%s\"\n\n", section);
        System.out.printf("Row Col Year Material Tracking\n");
        for (Panel panel : panels) {
            System.out.printf("%3d %3d %d %8s %8s\n",
                    panel.getRow(), panel.getColumn(),
                    panel.getYear(), panel.getType().getMaterialName(),
                    panel.isTracking() ? "yes" : "no");
        }
    }
    public void printPanelsOfMaterialType(List<Panel> panels) {
        if (panels.size() == 0) {
            printSubHeader("No Panels are of this material");
        } else {
            String material = panels.get(0).getType().getMaterialName();
            printSubHeader(String.format("\nMaterial Name: %s", material));
            System.out.printf("Panels with \"%s\" material\n\n", material);
            System.out.printf("Row Col Year Tracking Section\n");
            for (Panel panel : panels) {
                System.out.printf("%3d %3d %d %8s %s\n",
                        panel.getRow(), panel.getColumn(), panel.getYear(),
                        panel.isTracking() ? "yes" : "no", panel.getSection());
            }
        }
    }

    public Panel makePanel() {
        String section = console.readString("Section: ", true);
        int row = console.readPositiveInt("Row: ", true);
        int column = console.readPositiveInt("Column: ", true);
        MaterialType material = console.readMaterialType(true);
        int year = console.readPositiveInt("Installation Year: ", true);
        boolean tracking = console.readBoolean("Tracked [y/n]: ", true, false);
        return new Panel(section, row, column, material, year, tracking);
    }

    public Panel update(List<Panel> panels) {
        Panel panel = findPanel(panels);
        if (panel != null) {
            update(panel);
        }
        return panel;
    }
    private Panel update(Panel panel) {
        String section = panel.getSection();
        int row = panel.getRow();
        int column = panel.getColumn();
        MaterialType material = panel.getType();
        int year = panel.getYear();
        boolean tracking = panel.isTracking();
        System.out.printf("\nEditing %s-%d-%d\n", section, row, column);
        System.out.println("Press [Enter] to keep original value.\n");

        String tempString;
        int tempInt;
        MaterialType tempType;

        tempString = console.readString(String.format("Section (%s): ", section), false);
        section = tempString.isBlank() ? section : tempString;

        tempInt = console.readPositiveInt(String.format("Row (%d): ", row), false);
        row = tempInt == Integer.MAX_VALUE ? row : tempInt;

        tempInt = console.readPositiveInt(String.format("Column (%d): ", column), false);
        column = tempInt == Integer.MAX_VALUE  ? column : tempInt;

        System.out.printf("Material (%s):\n", panel.getType().getMaterialName());
        tempType = console.readMaterialType(false);
        material = tempType == null ? material : tempType;

        tempInt = console.readPositiveInt(String.format("Installation Year (%d): ", panel.getYear()), false);
        year = tempInt == Integer.MAX_VALUE  ? year : tempInt;

        tracking = console.readBoolean(String.format("Tracked (%s) [y/n]: ", panel.isTracking() ? "y" : "n"),false, tracking);

        panel.setSection(section);
        panel.setRow(row);
        panel.setColumn(column);
        panel.setType(material);
        panel.setYear(year);
        panel.setTracking(tracking);
        return panel;
    }
    public Panel findPanel(List<Panel> panels) {
        // all panels in same section
        String section = panels.get(0).getSection();
        System.out.println("\nProvide panel location");
        System.out.printf("Section: %s\n", section);
        int row = console.readPositiveInt("Row: ", true);
        int column = console.readPositiveInt("Column: ", true);
        for(Panel panel : panels) {
            if (panel.getRow() == row && panel.getColumn() == column) {
                return panel;
            }
        }
        System.out.printf("There is no panel %s-%d-%d\n", section, row, column);
        return null;
    }

    public void printResult(PanelResult result, String output) {
        if (result.isSuccess()) {
            Panel panel = result.getModule();
            System.out.println("\n[Success]");
            System.out.printf("Panel %s-%d-%d %s.\n", panel.getSection(), panel.getRow(), panel.getColumn(), output);
        } else {
            printErrors(result.getMessages());
        }
    }
    public void printHeader(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println("=".repeat(message.length()));
    }
    public void printSubHeader(String message) {
        System.out.println(message);
        System.out.println("~".repeat(message.length()));
    }
    public void printErrors(List<String> errors) {
        printSubHeader("\nErrors: ");
        for (String error : errors) {
            System.out.println(error);
        }
    }




}
