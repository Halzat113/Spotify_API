package com.spotify.tests;

import com.spotify.utils.ConfigurationReader;
import static io.restassured.RestAssured.*;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class base {

    protected static ValidatableResponse response;

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("uri");
        basePath = "/v1";
    }

    @AfterAll
    public static void teardown(){
        reset();
    }

}
