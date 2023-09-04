CREATE TABLE public.meteo_metrics (
	station varchar,
	measureTime timestamp,
    temperature numeric,
   	humidity numeric,
    dewPoint numeric,
   	windSpeed numeric,
    windDirection varchar,
   	pressure numeric,
    rain numeric,
    rainHour numeric
);