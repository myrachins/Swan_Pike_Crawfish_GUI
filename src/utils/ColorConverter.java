package utils;

import javafx.scene.paint.Color;

public class ColorConverter {
    public ColorConverter() { }

    public static String getColorName(Color color) {
        if (color.equals(Color.BLACK))
            return "BLACK";
        else if (color.equals(Color.BLUE))
            return "BLUE";
        else if (color.equals(Color.GREEN))
            return "GREEN";
        else if (color.equals(Color.CYAN))
            return "CYAN";
        else if (color.equals(Color.RED))
            return "RED";
        else if (color.equals(Color.YELLOW))
            return "YELLOW";
        else if (color.equals(Color.WHITE))
            return "WHITE";

        throw new IllegalArgumentException("By now this color is not supported");
    }
}
