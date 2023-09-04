package meteoscraper.imageparser;

/**
 * Represents a letter predicted or trained
 * @param letter: string representation of the letter
 * @param histogram: sum of pixel columns of the letter
 * @param accuracy: how precise was the prediction
 */
public record Letter(String letter, int[] histogram, double accuracy) {
    /**
     * Compare two lettersHistogram
     * @param otherHistogram: The other letterHistogram
     * @return from 0 to 1 how close two histograms are
     */
    public double compare(int[] otherHistogram) throws RuntimeException
    {
        if (this.histogram == null || this.histogram.length == 0)
            throw new RuntimeException("Empty histogram");

        double accumAbsError = 0;
        int accumThisTotal = 0;
        int shorterLength = Math.min(this.histogram.length, otherHistogram.length);
        for (int i=0; i<shorterLength; i++) {
            accumAbsError += Math.abs(this.histogram[i] - otherHistogram[i]);
            accumThisTotal += this.histogram[i];
        }
        double meanError =  1.0 - (accumAbsError / accumThisTotal);
        double lengthFactor = shorterLength / (double)this.histogram.length;
        return Math.max(0.0, meanError * lengthFactor);
    }

    /**
     * Compare two letters
     * @param other: The other letter
     * @return from 0 to 1 how close two histograms are
     */
    public double compare(Letter other) throws RuntimeException
    {
        return this.compare(other.histogram);
    }

    /**
     * @return histogram length
     */
    public int length()
    {
        return histogram.length;
    }
}
