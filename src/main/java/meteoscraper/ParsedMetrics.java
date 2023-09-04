package meteoscraper;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ParsedMetrics {
    public String station;
    public LocalDateTime measureTime;
    public double temperature;
    public int humidity;
    public double dewPoint;
    public double windSpeed;
    public String windDirection;
    public double pressure;
    public double rain;
    public double rainHour;

    static Pattern regexGroupsPattern = Pattern.compile("^([0-9.]*)°C([0-9.]*)%([0-9.]*)°C([0-9.]*)km/h([NSEW]*)([0-9.]*)hPa([0-9.]*)mm([0-9.]*)mm/h$");

    public ParsedMetrics(String _station, LocalDateTime _measureTime, double _temperature, int _humidity, double _dewPoint,
                         double _windSpeed, String _windDirection, double _pressure, double _rain, double _rainHour)
    {
        this.station = _station; this.measureTime = _measureTime;
        this.temperature = _temperature; this.humidity = _humidity;
        this.dewPoint = _dewPoint; this.windSpeed = _windSpeed;
        this.windDirection = _windDirection; this.pressure = _pressure;
        this.rain = _rain; this.rainHour = _rainHour;
    }

    public static ParsedMetrics fromCMLImageString(String station, LocalDateTime measureTime, String s)
    {
        Matcher m = ParsedMetrics.regexGroupsPattern.matcher(s);

        if (m.find()) {
            // for (int i=0; i <= m.groupCount(); i++)
            //    System.out.printf("Group(%d): %s\n", i, m.group(i));
            return new ParsedMetrics(
                    station, measureTime,
                    Double.parseDouble(m.group(1)), Integer.parseInt(m.group(2)),
                    Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)),
                    m.group(5), Double.parseDouble(m.group(6)),
                    Double.parseDouble(m.group(7)), Double.parseDouble(m.group(8))
            );
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ParsedMetrics castedObj) {
            return castedObj.station.equals(this.station)
                    && castedObj.measureTime.isEqual(this.measureTime)
                    && castedObj.temperature == this.temperature
                    && castedObj.humidity == this.humidity
                    && castedObj.dewPoint == this.dewPoint
                    && castedObj.windSpeed == this.windSpeed
                    && castedObj.windDirection.equals(this.windDirection)
                    && castedObj.pressure == this.pressure
                    && castedObj.rain == this.rain
                    && castedObj.rainHour == this.rainHour;
        } else {
            return false;
        }
    }

    @Override
    public String toString()
    {
        var sb = new StringBuilder();
        sb.append("{");
        sb.append(String.format("\"station\": \"%s\", ", this.station));
        sb.append(String.format("\"measureTime\": \"%s\", ", this.measureTime.toString()));
        sb.append(String.format("\"temperature\": %.1f, ", this.temperature));
        sb.append(String.format("\"humidity\": %d, ", this.humidity));
        sb.append(String.format("\"dewPoint\": %.1f, ", this.dewPoint));
        sb.append(String.format("\"windSpeed\": %.1f, ", this.windSpeed));
        sb.append(String.format("\"windDirection\": \"%s\", ", this.windDirection));
        sb.append(String.format("\"pressure\": %.1f, ", this.pressure));
        sb.append(String.format("\"rain\": %.1f, ", this.rain));
        sb.append(String.format("\"rainHour\": %.1f", this.rainHour));
        sb.append("}");
        return sb.toString();
    }

    public String toInsertQuery()
    {
        return String.format(
            "INSERT INTO public.meteo_metrics(station, measureTime, temperature, " +
                "humidity, dewPoint, windSpeed, windDirection, pressure, rain, rainHour) " +
                "VALUES ('%s', '%s', %.1f, %d, %.1f, %.1f, '%s', %.1f, %.1f, %.1f);",
            station, measureTime, temperature, humidity, dewPoint,
            windSpeed, windDirection, pressure, rain, rainHour
        );
    }
}
