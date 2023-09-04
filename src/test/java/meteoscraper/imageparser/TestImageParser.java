package meteoscraper.imageparser;

import meteoscraper.MeteoScraper;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestImageParser {

    @Test
    public void testImageToBWArray() throws IOException
    {
        var image = ImageIO.read(new File("/home/brunoaquino/repos/meteo-scraper/src/test/resources/simple_pixels.png"));
        var imgArray = ImageParser.imageToBWArray(image);
        int[][] expected = {
                {0, 0, 0, 0, 0}, {0, 1, 1, 0, 1}, {0, 0, 0, 0, 0}, {1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1}, {0, 0, 0, 0, 0}, {1, 1, 1, 1, 1}, {0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1}, {0, 0, 0, 0, 0}
        };
        assertArrayEquals(expected, imgArray);
    }

    @Test
    public void testToLettersHistogram()
    {
        int[][] imageArray = {
                {0, 0, 0, 0, 0}, {0, 1, 1, 0, 1}, {0, 0, 0, 0, 0}, {1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1}, {0, 0, 0, 0, 0}, {1, 1, 1, 1, 1}, {0, 1, 0, 0, 0},
                {1, 0, 1, 0, 1}, {0, 0, 0, 0, 0}
        };
        var lettersHist = ImageParser.toLettersHistogram(imageArray);
        int[][] expected = {{3}, {3, 3}, {5, 1, 3}};
        assertArrayEquals(expected, lettersHist);
    }

    @Test
    public void testImageToString() throws IOException, ParseException
    {
        var image = ImageIO.read(new File("/home/brunoaquino/repos/meteo-scraper/src/test/resources/v.png"));
        InputStream cmlTrainedModelIS = MeteoScraper.class.getClassLoader().getResourceAsStream("cml-trained-model.json");
        String cmlTrainedModel = new String(cmlTrainedModelIS.readAllBytes(), StandardCharsets.UTF_8);
        var ocrModel = new OcrModel(cmlTrainedModel);
        var predicted = ImageParser.imageToString(image, ocrModel);
        assertEquals("21.2°C54%11.5°C37.0km/hNE1000.4hPa44.0mm0.0mm/h", predicted);
    }
}
