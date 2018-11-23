/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain.shapes;

import ambrasiunas.exception.InvalidShapeParameterInputException;

import java.util.List;

public abstract class AnyShape implements Cloneable {

    private Integer shapeId;
    protected String shapeName;

    abstract String fillIdentifier();

    abstract Double fillArea();

    abstract Boolean checkArea(PlainCoordinate coordinate);

    abstract List<Double> fillParameters();

    public Boolean isInArea(PlainCoordinate plainCoordinate) {
        return checkArea(plainCoordinate);
    }

    public List<Double> getParameters(){
        return fillParameters();
    };

    public Double getArea() {
        return fillArea();
    }

    public String getIdentifier() {
        return fillIdentifier();
    }

    public Integer getId() {
        return shapeId;
    }

    public void setId(Integer id) {
        this.shapeId = id;
    }

    public String getShapeName() {
        return shapeName;
    }

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
