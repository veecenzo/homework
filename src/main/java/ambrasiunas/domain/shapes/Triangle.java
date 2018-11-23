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

public class Triangle extends AnyShape {

    private List<PlainCoordinate> vertex;
    private static Integer triangleVertecies = 3;

    public Triangle(List<Double> parameters) {
        validateParameters(parameters);
        shapeName = "triangle";
        setVertecies(parameters);
    }


    @Override
    List<Double> fillParameters() {
        List<Double> parameters = new ArrayList<>();
        parameters.add(vertex.get(0).getCordX());
        parameters.add(vertex.get(0).getCordY());
        parameters.add(vertex.get(1).getCordX());
        parameters.add(vertex.get(1).getCordY());
        parameters.add(vertex.get(2).getCordX());
        parameters.add(vertex.get(2).getCordY());
        return parameters;
    }

    @Override
    String fillIdentifier() {
        return "shape " + getId() + ": " + getShapeName() + " with vertices at points ("
                + vertex.get(0).getCordX() + " " + vertex.get(0).getCordY() + ") ( "
                + vertex.get(1).getCordX() + " " + vertex.get(1).getCordY() + ") ( "
                + vertex.get(2).getCordX() + " " + vertex.get(2).getCordY() + ") "
                + " fills area of " + fillArea() + "\n";

    }

    @Override
    Double fillArea() {
        Double triangleArea = (
                vertex.get(0).getCordX() * (vertex.get(1).getCordY() - vertex.get(2).getCordY()) +
                        vertex.get(1).getCordX() * (vertex.get(2).getCordY() - vertex.get(0).getCordY()) +
                        vertex.get(2).getCordX() * (vertex.get(0).getCordY() - vertex.get(1).getCordY())
        ) / 2;
        return Math.abs(triangleArea);
    }

    @Override
    Boolean checkArea(PlainCoordinate coordinate) {
        Boolean v1, v2, v3;

        v1 = Sign(coordinate, vertex.get(0), vertex.get(1)) < 0.0d;
        v2 = Sign(coordinate, vertex.get(1), vertex.get(2)) < 0.0d;
        v3 = Sign(coordinate, vertex.get(2), vertex.get(0)) < 0.0d;

        return ((v1 == v2) && (v2 == v3));

    }

    private Double Sign(PlainCoordinate vertex1, PlainCoordinate vertex2, PlainCoordinate vertex3) {
        return (vertex1.getCordX() - vertex3.getCordX()) * (vertex2.getCordY() - vertex3.getCordY())
                -
                (vertex2.getCordX() - vertex3.getCordX()) * (vertex1.getCordY() - vertex3.getCordY());
    }

    private void setVertecies(List<Double> parameters) {
        vertex = new ArrayList<>();
        Integer indexX = 0;
        Integer indexY = 1;
        for (Integer i = 0; i < triangleVertecies; i++) {
            vertex.add(new PlainCoordinate(parameters.get(indexX), parameters.get(indexY)));
            indexX = indexX + 2;
            indexY = indexY + 2;
        }
    }

    private void validateParameters(List<Double> parameters){
        if(null == parameters || parameters.size() < 6){
            throw new InvalidShapeParameterInputException("Triangle has to contain at least 6 parameters!");
        }
    }
}
