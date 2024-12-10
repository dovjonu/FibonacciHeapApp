package lt.dovydasjonuska.fibonacciheapapp.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HeapGuiApplication extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lt/dovydasjonuska/fibonacciheapapp/gui/layout.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);

        // Load dark theme
        scene.getStylesheets().add(getClass().getResource("/lt/dovydasjonuska/fibonacciheapapp/gui/dark-theme.css").toExternalForm());

        primaryStage.setTitle("Fibonacci Heap Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}