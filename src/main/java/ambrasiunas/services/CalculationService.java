/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.services;

import ambrasiunas.domain.shapes.AnyShape;

import java.util.List;

public interface CalculationService {

    List<AnyShape> isInArea(List<AnyShape> anyShapes);
}
