package controller;

import model.MainModel;
import view.DropboxView;
import view.GoogleDriveView;
import view.MainView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainController {
    private final MainModel mainModel;
    private final MainView mainView;

    public MainController(MainModel mainModel, MainView mainView) {
        this.mainModel = mainModel;
        this.mainView = mainView;

        mainView.addOptionSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = mainView.getSelectedOption();

                // Open new window based on the selected option
                if(selectedOption.equals("Dropbox")) {
                    new DropboxView();
                } else if(selectedOption.equals("Google Drive")) {
                    try {
                        new GoogleDriveView();
                    } catch (GeneralSecurityException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
