/*
 * Author Vytautas Ambrasiunas 2018.
 */

/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain.shapes;

public class PlainCoordinate {

    private Double cordX;
    private Double cordY;

    public PlainCoordinate(Double cordX, Double cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public Double getCordX() {
        return cordX;
    }

    public Double getCordY() {
        return cordY;
    }

    public void setCordX(Double cordX) {
        this.cordX = cordX;
    }

    public void setCordY(Double cordY) {
        this.cordY = cordY;
    }
}
