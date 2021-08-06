package edu.dev10.solarfarm.domain;

import edu.dev10.solarfarm.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelResult {
    private ArrayList<String> messages = new ArrayList<>();
    private Panel module;

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public Panel getModule() {
        return module;
    }

    public void setModule(Panel module) {
        this.module = module;
    }
}
