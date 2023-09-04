package meteoscraper.imageparser;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.util.ArrayList;

public class OcrModel {

    private ArrayList<Letter> model;

    /**
     * @param modelData: String containing json file with annotated letter histogram
     */
    public OcrModel(String modelData) throws ParseException
    {
        var jsonParser = new JSONParser();
        var jsonArray = (JSONArray) jsonParser.parse(modelData);
        this.model = new ArrayList<>();
        jsonArray.forEach(letter -> {
            var letterObj = (JSONObject)letter;
            var histJson = (JSONArray)letterObj.get("histogram");
            var histogram = new int[histJson.size()];
            for (int i=0; i < histJson.size(); i++)
                histogram[i] = ((Long)histJson.get(i)).intValue();
            this.model.add(new Letter((String)letterObj.get("letter"), histogram, 1));
        });
    }

    public int size()
    {
        return model.size();
    }

    /**
     * From a letter histogram gives the predicted letter
     * @param histogram: letter histogram fetched from image
     * @param accuracyToAccept: if any letter reach this accuracy is accepted (value from 0.0 to 1.0)
     * @param maxLengthDistance: if the lengths are too distant, it's not tested (value from 0.0 to 1.0)
     * @return Letter object containing the letter String predicted and accuracy
     */
    public Letter predictLetter(int[] histogram, double accuracyToAccept, double maxLengthDistance)
    {
        Letter betterLetter = null;
        double bestAccuracy = 0.0;
        for (Letter l : model)
        {
            if (histogram.length >= l.length() * (1 - maxLengthDistance)
                    && histogram.length <= l.length() * (1 + maxLengthDistance)) {
                var accuracy = l.compare(histogram);
                if (accuracy >= accuracyToAccept) {
                    return new Letter(l.letter(), histogram, accuracy);
                } else {
                    if (accuracy > bestAccuracy) {
                        betterLetter = l;
                        bestAccuracy = accuracy;
                    }
                }
            }
        }
        if (betterLetter == null)
            return new Letter("?", histogram, 0);
        else
            return new Letter(betterLetter.letter(), histogram, bestAccuracy);
    }

}
