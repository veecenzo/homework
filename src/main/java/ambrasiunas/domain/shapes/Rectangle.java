/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain.shapes;

import ambrasiunas.exception.InvalidShapeParameterInputException;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends AnyShape {
    private List<PlainCoordinate> coordinates;

    public Rectangle(List<Double> parameters) {
        validateParameters(parameters);
        shapeName = "rectangle";
        coordinates.add(new PlainCoordinate(parameters.get(0),parameters.get(1)));
        coordinates.add(new PlainCoordinate(parameters.get(2),parameters.get(3)));
    }

    @Override
    String fillIdentifier() {
        return "shape " + getId() + ": " + getShapeName() + " with vertices at points ("
                + coordinates.get(0).getCordX() + " " + coordinates.get(0).getCordY() + ") ( "
                + coordinates.get(1).getCordX() + " " + coordinates.get(1).getCordY() + ") "
                + " fills area of " + fillArea() + "\n";
    }

    @Override
    Double fillArea() {
        return (coordinates.get(0).getCordX() + coordinates.get(1).getCordX() ) * ( coordinates.get(0).getCordY()+coordinates.get(1).getCordY());
    }

    @Override
    Boolean checkArea(PlainCoordinate coordinate) {
        return null;
    }

    @Override
    List<Double> fillParameters() {
        List<Double> parameters = new ArrayList<>();
        parameters.add(coordinates.get(0).getCordX());
        parameters.add(coordinates.get(0).getCordY());
        parameters.add(coordinates.get(1).getCordX());
        parameters.add(coordinates.get(1).getCordY());
        return parameters;
    }

    private void validateParameters(List<Double> parameters){
        if(null == parameters || parameters.size() < 4){
            throw new InvalidShapeParameterInputException("Rectangle has to contain 4 parameters!");
        }
    }
}
