package meteoscraper.imageparser;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class ModelTrainerCLI {

    private static String jsonFromLetterHist(int[][] letterHist)
    {
        var jsonObj = new JSONObject();
        for (int i=0; i < letterHist.length; i++)
        {
            var jsonArray = new JSONArray();
            for (int el : letterHist[i])
                jsonArray.add(el);
            // TODO: change format as current cml-trained-model.json
            jsonObj.put(String.format("char-%d", i), jsonArray);
        }
        return jsonObj.toJSONString();
    }

    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.out.println("Requires two arguments, input image path and output json path");
            System.out.println("Example: parser-trainer ~/Downloads/v.png ~/Downloads/v.json");
            System.out.println("Exiting");
            System.exit(0);
        }
        var inputPath = args[0];
        var outputPath = args[1];
        try {
            var image = ImageIO.read(new File(inputPath));
            var image2DArray = ImageParser.imageToBWArray(image);
            var letterHist = ImageParser.toLettersHistogram(image2DArray);
            var trainingJson = jsonFromLetterHist(letterHist);
            var writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write(trainingJson);
            writer.close();
            System.out.printf("File %s successfully written!", outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
