/**
 * @author MM
 * @version 1.0
 */
public class PlateCost {
    private Long id;
    private Double x;
    private Double y;

    /**
     * Default constructor
     */
    public PlateCost() {
    }

    /**
     * @param id id
     * @param x x value
     * @param y y value
     */
    public PlateCost(Long id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return x value
     */
    public Double getX() {
        return x;
    }

    /**
     * @param x value
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * @return y value
     */
    public Double getY() {
        return y;
    }

    /**
     * @param y value
     */
    public void setY(Double y) {
        this.y = y;
    }
}
