plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    // TODO: change to Jackson: https://github.com/FasterXML/jackson-databind
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("org.postgresql:postgresql:42.6.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    applicationName = "meteo-scraper"
    mainClass.set("meteoscraper.MeteoScraper")
}

//application {
//    applicationName = "model-trainer"
//    mainClass.set("meteoscrapper.imageparser.ModelTrainerCLI")
//}

tasks.named<Test>("test") {
    useJUnitPlatform()
}