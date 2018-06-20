import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MM
 * @version 1.0
 */
public class MyPlate implements IMyPlate {
    private List<Double> horizontalCuts;
    private List<Double> verticalCuts;
    private Integer amountOfHorizontalCuts;
    private Integer amountOFVerticalCuts;

    private Integer amountOfHorizontalPieces = 1;
    private Integer amountOfVerticalPieces = 1;

    private Integer xIndex = 0;
    private Integer yIndex = 0;

    private Double costOfCuts = 0.0;

    /**
     * Default constructor
     */
    public MyPlate() {
        this.horizontalCuts = new ArrayList<>();
        this.verticalCuts = new ArrayList<>();
    }

    /**
     * @param plateCosts List of plateCosts
     * @return Cost Of All Cuts
     */
    @Override
    public Double calculateCostOfCuts(List<PlateCost> plateCosts) {
        splitIntoHorizontalAndVerticalCuts(plateCosts);
        calculateGlobalCuts();
        calculatePartialPiecesCost();

        return costOfCuts;
    }

    /**
     * Method split placeCosts into vertical and horizontal list
     * @param plateCosts List of plate cost
     */
    private void splitIntoHorizontalAndVerticalCuts(List<PlateCost> plateCosts) {
        for (PlateCost plateCost : plateCosts){
            if (plateCost.getX() != null && plateCost.getX() > 0){
                verticalCuts.add(plateCost.getX());
            }

            if (plateCost.getY() != null && plateCost.getY() > 0){
                horizontalCuts.add(plateCost.getY());
            }
        }
        verticalCuts.sort(Collections.reverseOrder());
        horizontalCuts.sort(Collections.reverseOrder());
        amountOfHorizontalCuts = horizontalCuts.size();
        amountOFVerticalCuts = verticalCuts.size();
    }

    /**
     * Calculate global cost of cuts
     */
    private void calculateGlobalCuts() {
        while (yIndex < amountOfHorizontalCuts
                && xIndex < amountOFVerticalCuts){
            Double horizontalCost = horizontalCuts.get(yIndex);
            Double verticalCost = verticalCuts.get(xIndex);

            if (horizontalCost > verticalCost){
                costOfCuts += horizontalCost * amountOfVerticalPieces;
                amountOfHorizontalPieces++;
                yIndex++;
            } else {
                costOfCuts += verticalCost * amountOfHorizontalPieces;
                amountOfVerticalPieces++;
                xIndex++;
            }
        }
    }

    /**
     * Calculate remaining pieces cost of cut
     */
    private void calculatePartialPiecesCost() {
        Double horizontalCutsCost =
                calculateCostInOneDirection(yIndex,
                        amountOfHorizontalCuts,
                        horizontalCuts);
        Double verticalCutCost =
                calculateCostInOneDirection(xIndex,
                        amountOFVerticalCuts,
                        verticalCuts);
        costOfCuts += horizontalCutsCost * amountOfVerticalPieces;
        costOfCuts += verticalCutCost * amountOfHorizontalPieces;
    }

    /**
     * @param index actual index
     * @param amountOfCuts amount of cuts
     * @param cuts list with veritical or horizontal cuts
     * @return partial cost of cuts
     */
    private Double calculateCostInOneDirection(Integer index, Integer amountOfCuts, List<Double> cuts) {
        Double partialCost = 0.0;

        for (int i = index; i < amountOfCuts; i++)
            partialCost += cuts.get(i);

        return partialCost;
    }
}
