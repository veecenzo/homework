/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.services;

import ambrasiunas.domain.shapes.AnyShape;
import ambrasiunas.domain.shapes.PlainCoordinate;

import java.util.List;

public interface ShapeService {

    AnyShape createAndSaveShape(AnyShape anyShape);

    List<AnyShape> getAllShapes();

    List<AnyShape> getShapesInCoordinate(PlainCoordinate plainCoordinate);

    void loadShapeFromFile();

}
