import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Runner;
import settings.AppSettings;
import settings.RunAttributes;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent settings = FXMLLoader.load(getClass().getResource("views/settings.fxml"));
        Parent menu = FXMLLoader.load(getClass().getResource("views/menu.fxml"));
        Parent canvas = FXMLLoader.load(getClass().getResource("views/canvas.fxml"));
        // TODO: make border pane with all views

        Group group = new Group(canvas);
        Scene scene = new Scene(group);

        primaryStage.setScene(scene);
        primaryStage.setTitle(AppSettings.PROGRAM_NAME);
        primaryStage.setWidth(AppSettings.APP_WIDTH);
        primaryStage.setHeight(AppSettings.APP_HEIGHT);
        primaryStage.show();

        Runner.start(new RunAttributes());
    }
}
