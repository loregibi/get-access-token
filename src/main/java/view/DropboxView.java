package view;

import controller.DropboxController;
import model.DropboxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DropboxView extends JFrame {
    private JButton authorizeButton;
    private JLabel statusLabel;

    public DropboxView() {
        DropboxModel dropboxModel = new DropboxModel();
        DropboxController dropboxController = new DropboxController(dropboxModel, this);

        setTitle("Dropbox Authorization");
        setSize(350,200);
        int x = 0;
        int y = 0;
        setLocation(x, y);

        // Load dropbox icon
        ImageIcon icon = new ImageIcon(DropboxView.class.getResource("/dropbox.png"));
        setIconImage(icon.getImage());

        authorizeButton = new JButton("Authorize Dropbox");
        statusLabel = new JLabel("Status: Not Authorized");

        authorizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String authorizeUrl = dropboxModel.startAuthorization();
                setStatus("Waiting for authorization...");
                openBrowser(authorizeUrl); // Open browser to authenticate

                String code = getCodeFromUser();
                if(code != null) {
                    dropboxController.authorizeDropbox(code);
                } else {
                    setStatus("Code not entered!");
                }
            }
        });

        setLayout(new BorderLayout());
        add(authorizeButton, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        setResizable(false);
        setVisible(true);
    }

    public void openBrowser(String url) {
        try {
            Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStatus(String status) {
        statusLabel.setText("Status: " + status);
    }

    public String getCodeFromUser() {
        String code = (String) JOptionPane.showInputDialog(
                null,
                "Enter the authorization code from Dropbox:",
                "Input",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "" // Default value
        );
        return code;
    }
}