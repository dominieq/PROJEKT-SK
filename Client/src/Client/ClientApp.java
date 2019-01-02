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
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Optional;

public class ClientApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController rootLayoutController;
    private ApplicationLayoutController applicationLayoutController;
    private Thread appThread;
    private volatile Socket clientSocket;
    private Boolean loopControlBoolean;
    private Boolean clearToCloseBoolean;

    @Override public void start(Stage primaryStage) {
        this.loopControlBoolean =  true;

        while (loopControlBoolean) {
            try {
                this.clientSocket = new Socket("localhost", 1234);
                this.clientSocket.setSoTimeout(5000);
                this.loopControlBoolean = false;
            } catch (IOException e) {
                showConnectionError("connect to");
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

    /**
     * Displays error alert. Gives two options. First to try again which will make no changes.
     * Second to exit anyway which will cause the application to exit with status 1.
     * If no option was chosen then the the application will exit with status 1
     */
    private void showConnectionError(String action) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to " + action + " server");
        alert.setHeaderText("You were unable to " + action + " server");

        ButtonType buttonTryAgain = new ButtonType("Try Again");
        ButtonType buttonExit = new ButtonType("Exit");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonTryAgain, buttonExit);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null || option.get() == buttonExit) {
            System.exit(1);
        }
    }

    public void showLogOutError(String error) {
        /* Set up alert features */
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to log out from server");
        alert.setHeaderText(error);

        ButtonType buttonTryAgain = new ButtonType("Try Again");
        ButtonType buttonGoBack = new ButtonType("Go Back");
        ButtonType buttonExit = new ButtonType("Exit Anyway");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonTryAgain, buttonGoBack, buttonExit);

        Optional<ButtonType> option = alert.showAndWait();

        /* Interpret user's choice */
        if (option.get() == null || option.get() == buttonExit) {
            this.loopControlBoolean = false;
        }
        else if (option.get() == buttonGoBack) {
            /* User wants to go back and continue working with application. */
            this.clearToCloseBoolean = false;
            this.loopControlBoolean = false;
        }
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
        try {

            msg = new String(buffer, "UTF-8");
            String[] parts = msg.split("END");
            msg = parts[0] + "END";

        } catch (UnsupportedEncodingException e) {
            return "CONVERTING_ERROR";
        }
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
        this.loopControlBoolean = true;
        while(loopControlBoolean) {
            try {
                this.clientSocket.close();
                this.loopControlBoolean = false;
            } catch (IOException exception) {
                showConnectionError("disconnect from");
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

    public ApplicationLayoutController getApplicationLayoutController() {
        return applicationLayoutController;
    }

    public Thread getAppThread() {
        return appThread;
    }

    public Boolean getClearToCloseBoolean() {
        return clearToCloseBoolean;
    }

    public void setClearToCloseBoolean(Boolean clearToCloseBoolean) {
        this.clearToCloseBoolean = clearToCloseBoolean;
    }

    public Boolean getLoopControlBoolean() {
        return loopControlBoolean;
    }

    public void setLoopControlBoolean(Boolean loopControlBoolean) {
        this.loopControlBoolean = loopControlBoolean;
    }
}
