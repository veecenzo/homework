/*
 * Author Vytautas Ambrasiunas 2018.
 */

package tests;

import ambrasiunas.domain.shapes.Circle;
import ambrasiunas.domain.shapes.Donut;
import ambrasiunas.domain.shapes.Triangle;
import ambrasiunas.exception.InvalidShapeParameterInputException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestShapeValidation {

    @Test(expected = InvalidShapeParameterInputException.class)
    public void testCircle() {
        Circle circle = new Circle(getInvalidParams());
    }

    @Test(expected = InvalidShapeParameterInputException.class)
    public void testDonut() {
        Donut donut = new Donut(getInvalidParams());
    }

    @Test(expected = InvalidShapeParameterInputException.class)
    public void testTriangle() {
        Triangle triangle = new Triangle(getInvalidParams());
    }

    private List<Double> getInvalidParams() {
        List<Double> badParams = new ArrayList<>();
        badParams.add(1d);
        return badParams;
    }
}
