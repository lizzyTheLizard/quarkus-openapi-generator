package io.quarkiverse.openapi.generator.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.acme.openapi.weather.api.CurrentWeatherDataApi;
import org.acme.openapi.weather.model.Model200;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTestResource(WiremockOpenWeather.class)
@QuarkusTest
public class OpenWeatherTest {

    // injected by quarkus test resource
    WireMockServer openWeatherServer;

    @ConfigProperty(name = "org.acme.openapi.weather.security.auth.app_id/api-key")
    String apiKey;

    @RestClient
    @Inject
    CurrentWeatherDataApi weatherApi;

    @Test
    public void testGetWeatherByLatLon() {
        final Model200 model = weatherApi.currentWeatherData("", "", "10", "-10", "", "", "", "");
        assertEquals("Nowhere", model.getName());
        openWeatherServer.verify(WireMock.getRequestedFor(
                WireMock.urlEqualTo("/weather?q=&id=&lat=10&lon=-10&zip=&units=&lang=&mode=&appid=" + apiKey)));
    }

}