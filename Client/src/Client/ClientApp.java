package Client;

import Client.view.ApplicationLayoutController;
import Client.view.LogInLayoutController;
import Client.view.RootLayoutController;
import Client.view.WelcomePageLayoutController;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ClientApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController rootLayoutController;
    private Socket clientSocket;
    private Boolean connectionControl;
    private Boolean clearToClose;

    @Override
    public void start(Stage primaryStage) {
        connectionControl =  true;

        while (connectionControl) {
            try {
                clientSocket = new Socket("localhost", 1234);
                connectionControl = false;
            } catch (IOException e) {
                showUnableToConnect();
            }
        }

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

    private void showUnableToConnect() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to connect to server");
        alert.setHeaderText("You are unable to connect to server");

        ButtonType buttonTryAgain = new ButtonType("Try Again");
        ButtonType buttonExit = new ButtonType("Exit");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonTryAgain, buttonExit);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            System.exit(1);
        }
        else if (option.get() == buttonExit) {
            System.exit(1);
        }
    }

    private void showUnableToDisconnect() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to disconnect to server");
        alert.setHeaderText("You were unable to disconnect from server");

        ButtonType buttonTryAgain = new ButtonType("Try Again");
        ButtonType buttonGoBack = new ButtonType("Go Back");
        ButtonType buttonExitAnyway = new ButtonType("Exit Anyway");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonTryAgain, buttonGoBack, buttonExitAnyway);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null || option.get() == buttonGoBack) {
            this.clearToClose = false;
            this.connectionControl = false;
        }
        else if (option.get() == buttonExitAnyway) {
            System.exit(1);
        }
    }

    public void closeConnection() {
        connectionControl = true;
        while(connectionControl) {
            try {
                clientSocket.close();
                connectionControl = false;
            } catch (IOException exception) {
                showUnableToDisconnect();
            }
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

    public Boolean getClearToClose() {
        return clearToClose;
    }

    public void setClearToClose(Boolean clearToClose) {
        this.clearToClose = clearToClose;
    }
}
