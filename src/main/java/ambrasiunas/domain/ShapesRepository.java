/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain;

import ambrasiunas.domain.shapes.AnyShape;
import org.omg.CORBA.Any;

import java.util.List;

public interface ShapesRepository {

    List<AnyShape> getPaginatedShapes();

    Integer  saveShape(AnyShape anyShape, String creatorInstance);
}
