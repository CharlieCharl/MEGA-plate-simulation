

import gui.controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import simulation.Simulation;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/MainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("MEGA plate simulation");
        primaryStage.setScene(new Scene(root, 1001, 351));
        primaryStage.setResizable(false);

        Simulation simulation = new Simulation();
        MainWindowController controller = loader.getController();
        controller.init(simulation);
        controller.start();
        simulation.start();

        primaryStage.show();
    }
        public static void main (String[]args){
            launch(args);
        }
    }

