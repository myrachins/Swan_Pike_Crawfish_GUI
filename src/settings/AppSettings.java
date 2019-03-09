package settings;

import javafx.scene.paint.Color;
import utils.ColorConverter;

public class AppSettings {
    private AppSettings() { }

    public static final int MIN_ANGLE = 0;
    public static final int MAX_ANGLE = 360;

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    public static final int TRUCK_WIDTH = 5;
    public static final int TRUCK_HEIGHT = 5;

    public static final int CREATURE_WIDTH = 10;
    public static final int CREATURE_HEIGHT = 10;

    public static final Color SWAN_COLOR = Color.BLUE;
    public static final Color PIKE_COLOR = Color.RED;
    public static final Color CRAWFISH_COLOR = Color.GREEN;

    public static final String PROGRAM_NAME = "Swan, Pike & Crawfish";
    public static final String ABOUT_PROGRAM =
            "This task is a part of the discipline \"Software Construction\", SE, HSE" +
                    System.lineSeparator() +
            "Program supports simulation of Krylov's fable: Swan, Pike & Crawfish" +
                    System.lineSeparator() +
            "Swan color: " + ColorConverter.getColorName(AppSettings.SWAN_COLOR) +
                    System.lineSeparator() +
            "Pike color: " + ColorConverter.getColorName(AppSettings.PIKE_COLOR) +
                    System.lineSeparator() +
            "Crawfish color: " + ColorConverter.getColorName(AppSettings.CRAWFISH_COLOR) +
                    System.lineSeparator() + System.lineSeparator() +
            "Program is made by Maxim Rachinskiy";
}
