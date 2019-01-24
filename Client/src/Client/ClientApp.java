package Client;

import Client.models.*;
import Client.view.ApplicationLayoutController;
import Client.view.LogInLayoutController;
import Client.view.RootLayoutController;
import Client.view.WelcomePageLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private Socket clientSocket;
    private Boolean clearToCloseBoolean;
    private volatile ObservableList<Tag> tagObservableList;
    private ObservableList<Tag> userTagObservableList;
    private ObservableList<Tag> choiceBoxTagsObservableList;
    private volatile ObservableList<Publication> publicationObservableList;

    @Override public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Publish/Subscribe Project");
        initRootLayout();
        showWelcomePageLayout();

    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/RootLayout.fxml"));
            this.rootLayout = loader.load();

            Scene scene = new Scene(this.rootLayout);
            this.primaryStage.setScene(scene);

            this.rootLayoutController = loader.getController();
            this.rootLayoutController.setClientApp(this);

            this.clearToCloseBoolean = true;
            this.tagObservableList = FXCollections.observableArrayList();
            this.userTagObservableList = FXCollections.observableArrayList();
            this.publicationObservableList = FXCollections.observableArrayList();
            this.choiceBoxTagsObservableList = FXCollections.observableArrayList();

            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void showWelcomePageLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/WelcomePageLayout.fxml"));
            AnchorPane layout = loader.load();

            this.rootLayout.setCenter(layout);

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
            AnchorPane layout = loader.load();

            this.rootLayout.setCenter(layout);

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
            TabPane layout = loader.load();

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


    public synchronized String messageStation(String action, String msg) {
        String ans = "";
        if(action.equals("snd")) {
            sendMessage(msg);
        }
        else if(action.equals("rcv")){
            ans = receiveMessage(msg);
        }
        return ans;
    }

    private String receiveMessage(String stage) {

        String msg;
        byte[] buffer = new byte[5000];
        try {

            InputStream is = this.clientSocket.getInputStream();
            if(is.read(buffer) == -1) {
                throw new IOException();
            }

        } catch (IOException exception) {

            if(exception instanceof SocketTimeoutException) {
                /*Timeout was exceeded and read function didn't receive any message*/
                System.out.println(stage + "TIMEOUT_ERROR");
                return "TIMEOUT_ERROR";
            }
            else {
                /*Any other possible error that may occur when using read function*/
                System.out.println(stage + "READ_ERROR");
                this.clientSocket = null;
                return "READ_ERROR";
            }

        }
        msg = new String(buffer);
        System.out.println(stage + msg);

        String[] parts = msg.split(";END");
        msg = parts[0] + ";END";
        return msg;
    }

    private void sendMessage(String msg) {

        try {
            OutputStream os = this.clientSocket.getOutputStream();
            System.out.println("Sending: " + msg);
            os.write(msg.getBytes());
        } catch (IOException ignored) {
            System.out.println("Sending failed msg = " + msg);
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

    public ObservableList<Tag> getTagObservableList() {
        return tagObservableList;
    }

    public ObservableList<Tag> getUserTagObservableList() {
        return userTagObservableList;
    }

    public ObservableList<Tag> getChoiceBoxTagsObservableList() {
        return choiceBoxTagsObservableList;
    }

    public ObservableList<Publication> getPublicationObservableList() {
        return publicationObservableList;
    }
}
