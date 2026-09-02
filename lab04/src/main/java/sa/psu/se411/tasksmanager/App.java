package sa.psu.se411.tasksmanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("TasksManager");

            StackPane root = new StackPane(new Label("TasksManager - Lab 04"));
            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
