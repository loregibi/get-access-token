package model;

import javax.swing.*;
import java.awt.*;

public class MainModel {
    private static final Font CUSTOM_FONT = new Font("Roboto",Font.PLAIN,13);
    private final String[] options = {"Dropbox","Google Drive"};

    public MainModel() {
        UIManager.put("Label.font", CUSTOM_FONT);
        UIManager.put("ComboBox.font", CUSTOM_FONT);
        UIManager.put("Button.font", CUSTOM_FONT);
    }

    public String[] getOptions() {
        return options;
    }
}
