package view;

import controller.MainController;
import model.MainModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JPanel {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private GridBagConstraints gbc;
    private JLabel label;
    private JComboBox<String> comboBox;

    public MainView() {
        MainModel mainModel = new MainModel();
        MainView mainView = new MainView(mainModel.getOptions());
        MainController mainController = new MainController(mainModel, mainView);

        // Create frame
        mainFrame = new JFrame("Access Token Generator");
        mainFrame.setSize(320,200);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load token icon
        ImageIcon icon = new ImageIcon(MainView.class.getResource("/token.png"));
        mainFrame.setIconImage(icon.getImage());

        // Create panel
        mainPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        // Create label
        label = new JLabel("Select service to use:");
        gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(label, gbc);

        // Add view to mainPanel
        gbc.gridy = 1;
        mainPanel.add(mainView, gbc);

        mainFrame.add(mainPanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public MainView(String[] options) {
        comboBox = new JComboBox<>(options);
        add(comboBox);
    }

    public String getSelectedOption() {
        return (String) comboBox.getSelectedItem();
    }

    public void addOptionSelectionListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }
}
