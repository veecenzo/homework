/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas;

import ambrasiunas.domain.shapes.AnyShape;
import ambrasiunas.domain.shapes.PlainCoordinate;
import ambrasiunas.services.*;
import ambrasiunas.utils.MenuUtils;
import ambrasiunas.exception.InvalidShapeParameterInputException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Application {

    private ShapeService shapeService;
    private ShapeFactory shapeFactory;

    public Application(ShapesServiceImpl shapesServiceImpl) {
        this.shapeService = shapesServiceImpl;
        this.shapeFactory = new ShapeFactory();
    }

    public static void main(String[] args) throws SQLException, IOException {
        Application app = new Application(new ShapesServiceImpl());
        app.initConsoleMenu();
    }

    public void initConsoleMenu() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\\s");

        System.out.print("Provide input below; type 'help' to get instructions.\n");
        Boolean leaveSession = false;
        String inputLine;
        String[] inputArray;
        List<Double> coordinateInput = null;
        List<Double> parameterInput = null;
        String commandInput;

        while (!leaveSession) {
            if (scanner.hasNext()) {

                inputLine = scanner.nextLine();
                inputArray = inputLine.trim().split("\\s+");
                commandInput = inputArray[0];
                coordinateInput = parseDoubleFromString(Arrays.asList(inputArray), false);
                parameterInput = parseDoubleFromString(Arrays.asList(inputArray), true);
                AnyShape anyShape = null;

                if (inputArray.length >= 3) {
                    try {
                        anyShape = shapeFactory.getShape(commandInput, parameterInput);
                        if (null == anyShape) {
                            System.out.print("Invalid shape name provided '" + commandInput + "'!, type 'shapes' to get all available shapes...\n");
                        }
                    } catch (InvalidShapeParameterInputException exception) {
                        System.out.print(exception.getMessage() + "\n");
                    }
                    if (null != anyShape) {
                        shapeService.createAndSaveShape(anyShape);
                        System.out.print(anyShape.getIdentifier());
                    }
                } else if (inputArray.length == 2 && null != coordinateInput) {
                    PlainCoordinate plainCoordinate = new PlainCoordinate(parameterInput.get(0), parameterInput.get(1));
                    List<AnyShape> shapesWithPlainCoordinate = shapeService.getShapesInCoordinate(plainCoordinate);
                    if (shapesWithPlainCoordinate.size() > 0) {
                        for (AnyShape anyShape1 : shapesWithPlainCoordinate) {
                            System.out.print("Point (" + plainCoordinate.getCordX() + " " + plainCoordinate.getCordY() + ") is inside shape's "
                                    + anyShape1.getId() + " :" + anyShape1.getShapeName() + " area " + Math.round(anyShape1.getArea()) + " \n");
                        }
                    } else {
                        System.out.print("No shapes were found in followin point (" + plainCoordinate.getCordX() + " " + plainCoordinate.getCordY() + ")\n");
                    }
                } else {
                    switch (commandInput.toLowerCase()) {
                        case "exit":
                            leaveSession = true;
                            break;
                        case "help":
                            System.out.println(MenuUtils.getHelpText());
                            break;
                        case "list":
                            List<AnyShape> allShapes = shapeService.getAllShapes();
                            if (null != allShapes && allShapes.size() > 0) {
                                for (AnyShape anyDbShape : allShapes) {
                                    System.out.print(anyDbShape.getIdentifier());
                                }
                            } else {
                                System.out.print("No shapes were found...\n");
                            }
                            break;
                        case "file":
                            shapeService.loadShapeFromFile();
                            break;
                        case "delete":
                            break;
                        case "shapes":
                            MenuUtils.getAvailableShapeTypes().stream().forEach(shape -> System.out.println(shape));
                            break;
                        default:
                            System.out.print("Unkonw input provided '" + commandInput + "'!, type 'help' to get all available input commands...\n");

                    }
                }
            } else {
                break;
            }
        }

    }

    private List<Double> parseDoubleFromString(List<String> parametersFromInput, Boolean ignoreString) {
        List<Double> parsedDoubles = new ArrayList<>();
        for (Integer i = 0; i < parametersFromInput.size(); i++) {

            try {
                parsedDoubles.add(Double.parseDouble(parametersFromInput.get(i)));
            } catch (NumberFormatException e) {
                if (ignoreString) {
                    continue;
                }
                return null;
            }

        }
        return parsedDoubles;
    }


}
