package meteoscraper.imageparser;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageParser {

    /**
     * Convert an image to and 2d array of 1 and 0 (1 if not white)
     *  The array is column based (the inner arrays ar columns)
     * @param img: some image
     * @return column based 2d arrays of 1 and 0
     */
    public static int[][] imageToBWArray(BufferedImage img)
    {
        final int width = img.getWidth();
        final int height = img.getHeight();

        var result = new int[width][height];
        for (int col=0; col < width; col++)
        {
            for (int row=0; row < height; row++)
            {
                int pixel = img.getRGB(col, row);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                result[col][row] = ((red + green + blue) / 3 == 255) ? 0 : 1;
            }
        }
        return result;
    }

    /**
     * From a column based array of 1 and 0 get and array of letters histogram.
     * It brakes the array by blank columns and calculate the histogram of each letter
     * @return letters histograms
     */
    public static int[][] toLettersHistogram(int[][] imageArray)
    {
        int[] columnsSum = new int[imageArray.length];
        for (int i=0; i < imageArray.length; i++)
        {
            columnsSum[i] = Arrays.stream(imageArray[i]).sum();
        }

        var result = new ArrayList<ArrayList<Integer>>();
        boolean hasLetter = false;
        for (int i=0; i < imageArray.length; i++)
        {
            if (columnsSum[i] == 0)
            {
                if (hasLetter)
                    hasLetter = false;
            }
            else
            {
                if (!hasLetter)
                {
                    hasLetter = true;
                    result.add(new ArrayList<>());
                }
                result.get(result.size() - 1).add(columnsSum[i]);
            }
        }
        return result.stream().map(  u  ->  u.stream().mapToInt(i->i).toArray()  ).toArray(int[][]::new);
    }

    private static String lettersHistToString(int[][] lettersHist, OcrModel model)
    {
        var strBuilder = new StringBuilder();
        for (int[] hist : lettersHist)
        {
            Letter l = model.predictLetter(hist, 1.0, 0.5);
            strBuilder.append(l.letter());
        }
        return strBuilder.toString();
    }

    public static String imageToString(BufferedImage image, OcrModel model)
    {
        var imgArray2d = imageToBWArray(image);
        var lettersHist = toLettersHistogram(imgArray2d);
        return lettersHistToString(lettersHist, model);
    }
}
