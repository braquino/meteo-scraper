FROM gradle:jdk17-jammy as builder

WORKDIR /opt/meteo-scraper-builder
COPY . .
RUN gradle install --no-daemon

FROM eclipse-temurin:17-jammy

WORKDIR /opt/meteo-scraper/bin
COPY --from=builder /opt/meteo-scraper-builder/build/install /opt/
COPY --from=builder /opt/meteo-scraper-builder/build/resources /opt/meteo-scraper
ENTRYPOINT ["./meteo-scraper"]
