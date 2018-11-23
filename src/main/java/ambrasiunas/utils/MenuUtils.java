/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.utils;

import java.util.ArrayList;
import java.util.List;

public final class MenuUtils {

    public static String getHelpText() {
        return "\n----Instructions----" +
                "\n1. To get all shapes in pointXnY input 2 decimal numbers" +
                "\n1.1 Example: '5.3 4'" +
                "\n2. To create a shape, provide shape name and parameters required" +
                "\n2.2 Example: 'circle 4.5 5.3 5.9'" +
                "\n3. To get a list of all available shapes, type 'list'" +
                "\n4. To load shapes from shapes.json, type 'file'" +
                "\n5. To get all current saved shapes, type 'shapes'" +
                "\n6. Type 'Exit' to terminate program session...\n";
    }

    public static List<String> getAvailableShapeTypes() {
        List<String> availableShapes = new ArrayList<>();
        availableShapes.add("circle   : circle pointX pointY radius");
        availableShapes.add("donut    : donut pointX pointY outerRadius innerRadius");
        availableShapes.add("triangle : triangle  vertexApointX  vertexApointY vertexBpointX  vertexBpointY vertexCpointX  vertexCpointY");
        return availableShapes;
    }
}
