package meteoscraper;

import meteoscraper.imageparser.ImageParser;
import meteoscraper.imageparser.OcrModel;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeteoScraper {

    final private static
    Pattern stationPattern = Pattern.compile("^http://rete\\.centrometeolombardo\\.com/.*/(.*)/immagini/v\\.png$");

    public static void main(String[] args)
    {
        try {
            InputStream cmlTrainedModelIS = MeteoScraper.class.getClassLoader().getResourceAsStream("cml-trained-model.json");
            String cmlTrainedModel = new String(cmlTrainedModelIS.readAllBytes(), StandardCharsets.UTF_8);

            InputStream urlListIS = MeteoScraper.class.getClassLoader().getResourceAsStream("image-url-list.txt");
            String[] urlList = new String(urlListIS.readAllBytes(), StandardCharsets.UTF_8).split("\n");

            var ocrModel = new OcrModel(cmlTrainedModel);

            var postgres = Postgres.fromEnvVars();
            postgres.connect();

            while (true)
            {
                for (var url : urlList)
                {
                    var img = ImageIO.read(new URL(url));
                    var imgStr = ImageParser.imageToString(img, ocrModel);
                    System.out.println(imgStr);
                    var parsedMetrics = ParsedMetrics.fromCMLImageString(MeteoScraper.getStation(url), LocalDateTime.now(), imgStr);
                    System.out.println(parsedMetrics);
                    var query = parsedMetrics.toInsertQuery();
                    System.out.println(query);
                    postgres.executeQuery(query);
                    //noinspection BusyWait
                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] loadUrlList(String urlListPath) throws IOException, ClassCastException
    {
        var l = Files.readAllLines(Paths.get(urlListPath));
        return l.toArray(String[]::new);
    }

    private static String getStation(String url)
    {
        Matcher m = MeteoScraper.stationPattern.matcher(url);
        if (m.find())
            return m.group(1);
        else
            return "???";
    }

}
