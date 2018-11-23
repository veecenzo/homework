/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain.shapes;

import ambrasiunas.exception.InvalidShapeParameterInputException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Circle extends AnyShape {

    private PlainCoordinate coordinates;
    private Double radius;

    public Circle(List<Double> parameters) {
        validateParameters(parameters);
        shapeName = "circle";
        coordinates = new PlainCoordinate(parameters.get(0), parameters.get(1));
        radius = parameters.get(2);
    }

    @Override
    Double fillArea() {
        return Math.abs(radius * radius * Math.PI);
    }

    @Override
    List<Double> fillParameters() {
        List<Double> parameters = new ArrayList<>();
        parameters.add(coordinates.getCordX());
        parameters.add(coordinates.getCordY());
        parameters.add(radius);
        return parameters;
    }

    @Override
    String fillIdentifier() {
        return "shape " + getId() + ": " + getShapeName() + " with centre at (" + coordinates.getCordX() + " " + coordinates.getCordY() + ") and radius " + radius + " fills area of " + Math.round(fillArea()) + " \n";
    }

    @Override
    Boolean checkArea(PlainCoordinate coordinate) {
        Double distance = Math.sqrt(
                Math.pow((coordinate.getCordX() - coordinates.getCordX()), 2) +
                        Math.pow((coordinate.getCordY() - coordinates.getCordY()), 2)
        );

        if (Math.abs(distance) <= Math.abs(radius)) {
            return true;
        }

        return false;
    }

    private void validateParameters(List<Double> parameters){
        if(null == parameters || parameters.size() < 3){
            throw new InvalidShapeParameterInputException("Circle has to contain at least 3 parameters!");
        }
    }
}