/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.services;

import ambrasiunas.domain.shapes.AnyShape;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Deprecated
public class ShapeCache {

    Map<Integer, AnyShape> shapeMap = new ConcurrentHashMap<Integer, AnyShape>();

    public AnyShape getShape(Integer shapeId) {
        AnyShape cachedAnyShape = shapeMap.get(shapeId);
        return cachedAnyShape != null ? (AnyShape) cachedAnyShape.clone() : null;
    }

    public synchronized Integer addShape(AnyShape anyShape) {
        Integer idIncrement = 0;
        Integer emptySlot = 0;
        for (Integer key : shapeMap.keySet()) {
            idIncrement = key;
        }
        idIncrement++;
        anyShape.setId(idIncrement);
        shapeMap.putIfAbsent(idIncrement, anyShape);
        return idIncrement;
    }

    public List<AnyShape> getAllShapes() {
        List<AnyShape> anyShapes = new ArrayList<>();
        for (Map.Entry<Integer, AnyShape> entry : shapeMap.entrySet()) {
            anyShapes.add(entry.getValue());
        }
        return anyShapes;
    }

    public AnyShape getShapeById(Integer shapeId) {
        AnyShape cachedAnyShape = shapeMap.get(shapeId);
        return cachedAnyShape != null ? (AnyShape) cachedAnyShape.clone() : null;
    }

    public void removeShape(Integer shapeId) {
        for (Map.Entry<Integer, AnyShape> entry : shapeMap.entrySet()) {
            if (entry.getKey().equals(shapeId)) {
                shapeMap.remove(shapeId);
            }
        }
    }

    public void cleanCache() {
        for (Map.Entry<Integer, AnyShape> entry : shapeMap.entrySet()) {
            shapeMap.remove(entry.getKey());
        }
    }

}