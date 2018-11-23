/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain.shapes;

import ambrasiunas.exception.InvalidShapeParameterInputException;

import java.util.ArrayList;
import java.util.List;

public class Donut extends AnyShape {

    private PlainCoordinate coordinates;
    private Double innerRadius;
    private Double outerRadius;

    public Donut(List<Double> parameters) {
        validateParameters(parameters);
        shapeName = "donut";
        coordinates = new PlainCoordinate(parameters.get(0), parameters.get(1));
        outerRadius = parameters.get(2);
        innerRadius = parameters.get(3);

    }

    private Double calculateInnerRadius() {
        return innerRadius * innerRadius * Math.PI;
    }

    private Double calculateOuterRadius() {
        return outerRadius * outerRadius * Math.PI;
    }

    @Override
    Double fillArea() {
        return calculateOuterRadius() - calculateInnerRadius();
    }

    @Override
    List<Double> fillParameters() {
        List<Double> parameters = new ArrayList<>();
        parameters.add(coordinates.getCordX());
        parameters.add(coordinates.getCordY());
        parameters.add(outerRadius);
        parameters.add(innerRadius);
        return parameters;
    }

    @Override
    String fillIdentifier() {
        return "shape " + getId() + ": " + getShapeName() + " with centre at (" + coordinates.getCordX() + " " + coordinates.getCordY() + ") and radiuses ( " + outerRadius + " " + innerRadius + " ) fills area of " + Math.round(fillArea()) + " \n";
    }

    @Override
    Boolean checkArea(PlainCoordinate coordinate) {
        Double distance = Math.sqrt(
                Math.pow((coordinate.getCordX() - coordinates.getCordX()), 2) +
                        Math.pow((coordinate.getCordY() - coordinates.getCordY()), 2)
        );

        if (Math.abs(distance) <= Math.abs(outerRadius) && Math.abs(distance) >= Math.abs(innerRadius)) {
            return true;
        }

        return false;
    }

    private void validateParameters(List<Double> parameters){
        if(null == parameters || parameters.size() < 4){
            throw new InvalidShapeParameterInputException("Donut has to contain at least 4 parameters!");
        }
        if (parameters.get(3) <= 0 || parameters.get(2) <= 0)
            throw new InvalidShapeParameterInputException("Donut inner/outter radius has to be a positive value and greater than 0!");
        if (parameters.get(2) < parameters.get(3))
            throw new InvalidShapeParameterInputException("Donut outter radius has to be greater than inner!");
    }
}
