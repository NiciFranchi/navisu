/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.tools.impl.controller;

import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.app.guiagent.options.domain.Option;
import bzh.terrevirtuelle.navisu.app.guiagent.options.domain.SerialDeviceOption;
import bzh.terrevirtuelle.navisu.app.guiagent.options.domain.UserOption;
import bzh.terrevirtuelle.navisu.app.guiagent.options.domain.UserOptionBuilder;
import static bzh.terrevirtuelle.navisu.app.guiagent.utilities.Translator.tr;
import bzh.terrevirtuelle.navisu.tools.impl.ToolsComponentImpl;
import bzh.terrevirtuelle.navisu.widgets.impl.Widget2DController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author serge
 * @date May 21, 2016
 *
 */
public class ToolsComponentController
        extends Widget2DController
        implements Initializable {

    GuiAgentServices guiAgentServices;
    
    private final String FXML = "toolsController.fxml";
    protected String CONFIG_FILE_NAME = System.getProperty("user.home") + "/.navisu/config/config.properties";

    protected Properties properties;
    private static final String CSS_STYLE_PATH = Paths.get(System.getProperty("user.dir") + "/css/").toUri().toString();
    private static ToolsComponentController INSTANCE;
    private final ToolsComponentImpl component;
    protected String viewgroupstyle = "configuration.css";

    /* Common controls */
    @FXML
    public Group view;
    @FXML
    public Pane viewPane;
    @FXML
    public TabPane toolsTabPane;
    @FXML
    public Button quit;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Button defaultButton;
    @FXML
    public Button helpButton;

    /* enc controls */
    @FXML
    public Tab encDBTab;
    @FXML
    public TextField encHomeTF;
    @FXML
    public TextField countryTF;
    @FXML
    public TextField dataS57CatalogTF;
    @FXML
    public Button encButton;
    
    /* Bathy controls */
    @FXML
    public Tab bathyDBTab;
    
    String encPath;
    String countryPath;
    
    String encPathOld;
    String countryPathOld;
    
    private ObservableList<String> countryCbData = FXCollections.observableArrayList();
    
    protected FileChooser fileChooser;

    /**
     *
     * @param component
     * @param keyCode
     * @param keyCombination
     * @param guiAgentServices
     */
    @SuppressWarnings("unchecked")
    public ToolsComponentController(ToolsComponentImpl component,
            KeyCode keyCode, KeyCombination.Modifier keyCombination,
            GuiAgentServices guiAgentServices) {
        super(keyCode, keyCombination);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToolsComponentController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        String uri = CSS_STYLE_PATH + viewgroupstyle;
        view.getStylesheets().add(uri);
        this.component = component;
        this.guiAgentServices = guiAgentServices;
        
        guiAgentServices.getScene().addEventFilter(KeyEvent.KEY_RELEASED, this);
        guiAgentServices.getRoot().getChildren().add(this);
       
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /* Init userTab and ownerShipTab 
           Values are store in CONFIG_FILE_NAME 
           properties file
        */
        properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE_NAME));
        } catch (IOException ex) {
            Logger.getLogger(ToolsComponentController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
       
        encHomeTF.setText(properties.getProperty("s57ChartsDir").trim());
       
        encPathOld = encHomeTF.getText().trim();
        
        try {
            properties.store(new FileOutputStream(CONFIG_FILE_NAME), null);
        } catch (IOException ex) {
            Logger.getLogger(ToolsComponentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        

        quit.setOnMouseClicked((MouseEvent event) -> {
            component.off();
        });
        
        saveButton.setOnMouseClicked((MouseEvent event) -> {
            if (encDBTab.isSelected()) {
                encPath = encHomeTF.getText();
                
                saveUser();
            }
            

        });

        cancelButton.setOnMouseClicked((MouseEvent event) -> {
            if (bathyDBTab.isSelected()) {
                encHomeTF.setText(encPathOld);
               
                saveUser();
            }
            
        });
        defaultButton.setOnMouseClicked((MouseEvent event) -> {
            if (encDBTab.isSelected()) {
                encPath = "";
                encHomeTF.setText(encPath);
               
                saveUser();
            }
            
        });
        helpButton.setOnMouseClicked((MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Options");
            alert.setHeaderText("Données utilisateur");
            Text s = new Text("    S57ChartsDir : chemin de la racine des cartes S57 \n");
                 
                    
            s.setWrappingWidth(650);
            alert.getDialogPane().setContent(s);
            alert.show();
        });
        encButton.setOnMouseClicked((MouseEvent event) -> {
            openDir(encHomeTF);
        });
        

    }

    private void saveUser() {
        /*
        try (OutputStream output = new FileOutputStream(CONFIG_FILE_NAME)) {
            properties.setProperty("s57ChartsDir", encPath);
            
            properties.store(output, null);
            output.close();

            UserOption userOption = UserOptionBuilder.create()
                    .s57Path(s57Path)
                    .build();
            notifyConfEvent(userOption);
        } catch (IOException ex) {
            Logger.getLogger(ToolsComponentController.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
*/
    }

    


    

    public void openFile(TextField tf) {
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle(tr("popup.fileChooser.open"));
        String userInitialDirectory = System.getProperty("user.home");
        this.fileChooser.setInitialDirectory(new File(userInitialDirectory));
        File selectedFile = this.fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            tf.setText("No file selected");
        } else {
            tf.setText(selectedFile.getAbsolutePath());
        }
    }

    public void openDir(TextField tf) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(tr("popup.fileChooser.open"));
        String userInitialDirectory = System.getProperty("user.home");
        directoryChooser.setInitialDirectory(new File(userInitialDirectory));
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            tf.setText("No Directory selected");
        } else {
            tf.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void notifyConfEvent(Option option) {

        try {
           // component.notifyConfEvent(option);
        } catch (Exception e) {

        }
    }
}