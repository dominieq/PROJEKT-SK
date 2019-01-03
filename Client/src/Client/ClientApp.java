package Client;

import Client.view.ApplicationLayoutController;
import Client.view.LogInLayoutController;
import Client.view.RootLayoutController;
import Client.view.WelcomePageLayoutController;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController rootLayoutController;
    private ApplicationLayoutController applicationLayoutController;
    private Thread appThread;
    private volatile Socket clientSocket;
    private Boolean clearToCloseBoolean;

    @Override public void start(Stage primaryStage) {

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

            Scene scene = new Scene(this.rootLayout);
            this.primaryStage.setScene(scene);

            this.rootLayoutController = loader.getController();
            this.rootLayoutController.setClientApp(this);

            this.clearToCloseBoolean = true;

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

            this.rootLayout.setCenter(layout);

            WelcomePageLayoutController controller = loader.getController();
            controller.setApp(this);
            controller.setUp();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showLogInLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/LogInLayout.fxml"));
            AnchorPane layout = (AnchorPane) loader.load();

            this.rootLayout.setCenter(layout);

            LogInLayoutController controller = loader.getController();
            controller.setApp(this);
            controller.setUp();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showApplicationLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/ApplicationLayout.fxml"));
            TabPane layout = (TabPane) loader.load();

            this.rootLayout.setCenter(layout);

            this.applicationLayoutController = loader.getController();
            this.applicationLayoutController.setApp(this);
            this.applicationLayoutController.setUp();
            /*Start ApplicationLayoutController thread*/
            this.appThread = new Thread(this.applicationLayoutController);
            this.appThread.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showError(String title, String error) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(error);
        alert.showAndWait();

    }


    public synchronized String receiveMessage() {
        String msg;
        byte[] buffer = new byte[5000];
        try {

            InputStream is = this.clientSocket.getInputStream();
            is.read(buffer);

        } catch (IOException exception) {

            if(exception instanceof SocketTimeoutException) {
                return "TIMEOUT_ERROR";
            }
            else {
                return "READ_ERROR";
            }

        }
        msg = new String(buffer);
        String[] parts = msg.split("END");
        msg = parts[0] + "END";
        return msg;
    }

    public synchronized void sendMessage(String msg) {
        try {
            OutputStream os = this.clientSocket.getOutputStream();
            os.write(msg.getBytes());
        } catch (IOException e) {
            // TO DO
            // Function should display alert
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {

            this.clientSocket.close();

        } catch (Exception exception) {

            if(exception instanceof NullPointerException) {
                this.clearToCloseBoolean = true;
            }
            this.clearToCloseBoolean = false;

        }
        this.clearToCloseBoolean = true;
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

    public ApplicationLayoutController getApplicationLayoutController() {
        return applicationLayoutController;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Thread getAppThread() {
        return appThread;
    }

    public Boolean getClearToCloseBoolean() {
        return clearToCloseBoolean;
    }
}
