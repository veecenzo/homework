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

class Square extends AnyShape {

    private List<PlainCoordinate> coordinates;

    public Square(List<Double> parameters) {
        validateParameters(parameters);
        shapeName = "rectangle";
        coordinates = new ArrayList<>();
        coordinates.add(new PlainCoordinate(parameters.get(0),parameters.get(1)));
        coordinates.add(new PlainCoordinate(parameters.get(1),parameters.get(2)));
    }

    @Override
    String fillIdentifier() {
        return null;
    }

    @Override
    Double fillArea() {
        return null;
    }

    @Override
    Boolean checkArea(PlainCoordinate coordinate) {
        return null;
    }

    @Override
    List<Double> fillParameters() {
        return null;
    }

    private void validateParameters(List<Double> parameters){
        if(null == parameters || parameters.size() < 2){
            throw new InvalidShapeParameterInputException("Square has to contain 4 parameters!");
        }else if(!((parameters.get(0) + parameters.get(1)) != (parameters.get(2)+ parameters.get(3)))){
            throw new InvalidShapeParameterInputException("Square has to have equal width and lenght!");
        }
    }
}
