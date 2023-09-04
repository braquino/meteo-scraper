package meteoscraper.imageparser;

import meteoscraper.MeteoScraper;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOcrModel {

    @Test
    public void testOcrModel() throws IOException, ParseException
    {
        InputStream cmlTrainedModelIS = MeteoScraper.class.getClassLoader().getResourceAsStream("cml-trained-model.json");
        String cmlTrainedModel = new String(cmlTrainedModelIS.readAllBytes(), StandardCharsets.UTF_8);
        var ocrModel = new OcrModel(cmlTrainedModel);
        assertEquals(33, ocrModel.size());
        var predictedLetter = ocrModel.predictLetter(new int[]{2,4,6,5,4,3,3,4,5,6}, 1, 0.5);
        assertEquals("°", predictedLetter.letter());
        assertEquals(0.8677685950413223, predictedLetter.accuracy());
        var predictedLetter2 = ocrModel.predictLetter(new int[]{2,4,6,5,4,3,3,4,5,6}, 1, 0.0);
        assertEquals("?", predictedLetter2.letter());
        assertEquals(0.0, predictedLetter2.accuracy());
    }
}
