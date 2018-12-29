package Client;

import Client.view.ApplicationLayoutController;
import Client.view.LogInLayoutController;
import Client.view.RootLayoutController;
import Client.view.WelcomePageLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController rootLayoutController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Publish/Subscribe Project");

        initRootLayout();

        showWelcomePageLayout();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/RootLayout.fxml"));
            this.rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            rootLayoutController = loader.getController();
            rootLayoutController.setClientApp(this);

            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showWelcomePageLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/WelcomePageLayout.fxml"));
            AnchorPane layout = (AnchorPane) loader.load();

            rootLayout.setCenter(layout);

            WelcomePageLayoutController controller = loader.getController();
            controller.setApp(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showLogInLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/LogInLayout.fxml"));
            AnchorPane layout = (AnchorPane) loader.load();

            rootLayout.setCenter(layout);

            LogInLayoutController controller = loader.getController();
            controller.setApp(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showApplicationLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/ApplicationLayout.fxml"));
            TabPane layout = (TabPane) loader.load();

            rootLayout.setCenter(layout);

            ApplicationLayoutController controller = loader.getController();
            controller.setApp(this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public ClientApp() {

    }
    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }
}
