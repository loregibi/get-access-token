package view;

import controller.GoogleDriveController;
import model.GoogleDriveModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleDriveView extends JFrame {
    private final JButton authorizeButton;
    private final JLabel statusLabel;

    public GoogleDriveView() throws GeneralSecurityException, IOException {
        GoogleDriveModel googleDriveModel = new GoogleDriveModel();
        GoogleDriveController googleDriveController = new GoogleDriveController(googleDriveModel, this);

        setTitle("Google Drive Authorization");
        setSize(350,200);

        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - this.getWidth();
        int y = 0;
        setLocation(x, y);

        // Load googledrive icon
        ImageIcon icon = new ImageIcon(GoogleDriveView.class.getResource("/googledrive.png"));
        setIconImage(icon.getImage());

        authorizeButton = new JButton("Authorize GoogleDrive");
        statusLabel = new JLabel("Status: Not Authorized");

        authorizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String authorizeUrl = GoogleDriveModel.startAuthorization();
                setStatus("Waiting for authorization...");
                openBrowser(authorizeUrl); // Open browser to authenticate

                String code = getCodeFromUser();
                if(code != null) {
                    googleDriveController.authorizeGoogleDrive(code);
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
                "Enter the authorization code from GoogleDrive:",
                "Input",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "" // Default value
        );
        return code;
    }
}
