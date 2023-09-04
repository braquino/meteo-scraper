package meteoscraper.imageparser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLetter {

    @Test
    public void testCompare()
    {
        var letter = new Letter("b", new int[]{2, 5, 7, 8, 4, 5}, 1);
        assertEquals(1.0, letter.compare(new int[]{2, 5, 7, 8, 4, 5}));
        assertEquals(0.6451612903225806, letter.compare(new int[]{1, 2, 10, 4, 4, 5}));
        assertEquals(0.0, letter.compare(new int[]{500, 2, 10, 4, 4, 5}));
        var otherLetter = new Letter("c", new int[]{1, 2, 10, 4, 4}, 1);
        assertEquals(0.4807692307692307, letter.compare(otherLetter));
    }
}
