package meteoscraper;

import meteoscraper.ParsedMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.time.LocalDateTime;

public class TestParsedMetrics {

    @Test
    public void testFromCMLImageString()
    {
        var parsedMetrics = ParsedMetrics.fromCMLImageString(
                "station-x",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                "21.2°C54%11.5°C37.0km/hNE1000.4hPa44.0mm0.0mm/h");
        var expected = new ParsedMetrics(
                "station-x", LocalDateTime.of(2023, 1, 1, 0, 0),
                21.2, 54, 11.5, 37.0, "NE", 1000.4,
                44.0, 0.0);
        assertEquals(expected, parsedMetrics);

        var parsedMetrics2 = ParsedMetrics.fromCMLImageString(
                "station-x2",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                "16.7°C77%12.6°C4.8km/hNNE1003.7hPa0.3mm0.0mm/h");
        var expected2 = new ParsedMetrics(
                "station-x2", LocalDateTime.of(2023, 1, 1, 0, 0),
                16.7, 77, 12.6, 4.8, "NNE", 1003.7,
                0.3, 0.0);
        assertEquals(expected2, parsedMetrics2);
    }
}
