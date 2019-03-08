import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import settings.AppSettings;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent settings = FXMLLoader.load(getClass().getResource("views/settings.fxml"));
        Parent menu = FXMLLoader.load(getClass().getResource("views/menu.fxml"));
        Parent canvas = FXMLLoader.load(getClass().getResource("views/canvas.fxml"));
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setTop(menu);
        root.setRight(settings);

        Group group = new Group(root);
        Scene scene = new Scene(group);

        primaryStage.setScene(scene);
        primaryStage.setTitle(AppSettings.PROGRAM_NAME);
        primaryStage.setWidth(AppSettings.APP_WIDTH);
        primaryStage.setHeight(AppSettings.APP_HEIGHT);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }
}
