module lt.dovydasjonuska.fibonacciheapapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;

    // Allow JavaFX to access the `gui` package
    opens lt.dovydasjonuska.fibonacciheapapp.gui to javafx.fxml;

    // Export packages to be used outside the module
    exports lt.dovydasjonuska.fibonacciheapapp.utils;
    exports lt.dovydasjonuska.fibonacciheapapp.gui;
}
