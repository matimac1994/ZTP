import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebClient {

    public static void main(String[] args){

        try {
            checkNumberOfArgs(args);

            List<String> listOfPeople = getListOfPeopleFromFile(args[0]);

            int numberOfLine = findBestLine(listOfPeople, args[1]);

            System.out.println(numberOfLine);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkNumberOfArgs(String[] args) {
        if (args.length != 2){
            throw new IllegalArgumentException("Too few arguments, required 2");
        }
    }

    private static List<String> getListOfPeopleFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return lines;
    }

    private static int findBestLine(List<String> listOfPeople, String lineFromArgs) {
        int bestDistance, bestDistanceLineNumber;
        int currentDistance;
        int numberOfLine = 0;
        bestDistance = bestDistanceLineNumber = Integer.MAX_VALUE;

        lineFromArgs = splitStringAndSort(lineFromArgs.toLowerCase());

        for (String lineFromFile : listOfPeople){
            numberOfLine++;

            lineFromFile = splitStringAndSort(lineFromFile.toLowerCase());

            if (lineFromFile.length() > lineFromArgs.length()){
                lineFromArgs = fillSpecialChars(lineFromArgs, lineFromFile.length() - lineFromArgs.length());
            } else {
                lineFromFile = fillSpecialChars(lineFromFile, lineFromArgs.length() - lineFromFile.length());
            }

            currentDistance = Hamming.calculateDistance(lineFromFile, lineFromArgs);

            if (currentDistance == 0){
                bestDistanceLineNumber = numberOfLine;
                break;
            }

            if (currentDistance < bestDistance){
                bestDistance = currentDistance;
                bestDistanceLineNumber = numberOfLine;
            }
        }
        return bestDistanceLineNumber;
    }

    private static String splitStringAndSort(String lineFromArgs) {
        String[] substrings = lineFromArgs.split("\\s");
        Arrays.sort(substrings);
        lineFromArgs = Arrays.toString(substrings);
        return lineFromArgs;
    }

    private static String fillSpecialChars(String line, int i) {
        StringBuilder builder = new StringBuilder();
        builder.append(line);
        for (int j = 0; j < i; j++){
            builder.append("$");
        }
        return builder.toString();
    }
}
