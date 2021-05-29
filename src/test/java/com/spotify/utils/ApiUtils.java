package com.spotify.utils;



import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiUtils {

    public static String getAccessToken() {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .header("Authorization", "Basic " + ConfigurationReader.getProperty("client_credentials"))
                .formParam("grant_type", "refresh_token")
                .formParam("refresh_token", ConfigurationReader.getProperty("refresh_token")).
                        when()
                .post("https://accounts.spotify.com/api/token").
                        then()
                .statusCode(200)
                .extract().jsonPath().getString("access_token");
    }

    public static RequestSpecification givenSpec() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAccessToken());
    }

    public static ValidatableResponse get(String endPoint) {
        return givenSpec().get(endPoint).
        then().spec(thenSpec(200));
    }

    public static ValidatableResponse post(String endPoint, Object payload) {

        return given()
                .spec(givenSpec())
                .body(payload).
                        when()
                .post(endPoint)
                .then().spec(thenSpec(201));
    }



    public static void put(String endpoint, Object payload) {
        givenSpec().body(payload).put(endpoint).then().spec(thenSpec(200)).log().ifError();
    }

    public static ResponseSpecification thenSpec(int statusCode) {
        return expect().statusCode(is(statusCode));
    }

    public static void getInfoValidation(ValidatableResponse response,Object collaborative,Object description,Object name){
        response.assertThat().body("collaborative", is(collaborative))
                .body("description", is(description))
                .body("name", is(name));
    }


    public static String playlistItemsPayload(List<String> tracks){
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        tracks.forEach(jsonArray::put);
        object.put("uris",jsonArray);
        return object.toString();
    }

    public static Map<String,Integer> reorderPayload(int rangeStart,int insertBefore){
        Map<String,Integer> payload = new LinkedHashMap<>();
        payload.put("range_start",rangeStart);
        payload.put("insert_before",insertBefore);
        return payload;
    }
}
