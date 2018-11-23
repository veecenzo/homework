/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.services;

import ambrasiunas.domain.shapes.Circle;
import ambrasiunas.domain.shapes.AnyShape;
import ambrasiunas.domain.shapes.Donut;
import ambrasiunas.domain.shapes.Triangle;
import ambrasiunas.domain.shapes.Rectangle;

import java.util.List;

public class ShapeFactory {

    public AnyShape getShape(String shapeType, List<Double> parameters){
        if(shapeType.equalsIgnoreCase("circle")){
            return new Circle(parameters);
        } else if(shapeType.equalsIgnoreCase("donut")){
            return new Donut(parameters);
        } else if(shapeType.equalsIgnoreCase("triangle")){
            return new Triangle(parameters);
        }else if(shapeType.equalsIgnoreCase("rectangle")){
            return new Rectangle(parameters);
        }else if(shapeType.equalsIgnoreCase("square")){
            return new Triangle(parameters);
        }

        return null;
    }

}
