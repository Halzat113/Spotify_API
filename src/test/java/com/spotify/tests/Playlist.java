package com.spotify.tests;

import static com.spotify.utils.ApiUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.spotify.pojos.Items;
import com.spotify.pojos.PlayList;
import com.spotify.utils.CsvUtil;
import com.spotify.utils.GlobalData;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Playlist extends base {

    static GlobalData data = new GlobalData();

    @Order(1)
    @DisplayName("1.Create a new play list")
    @Test
    public void createPlayList() {
        String endPoint = "users/" + data.getUser_id() + "/playlists";
        data.setPlayList("API Test", "New playlist description", true);
        response = post(endPoint, data.getPlayList());
        data.setPlayList(response.extract().as(PlayList.class));
        data.setPlaylistId(response.extract().path("id"));
    }

    @Order(2)
    @DisplayName("2.Verify user's info")
    @Test
    public void userInfoVerification() {
        response.assertThat().body("owner.display_name", is(data.getPlayList().getUser().getUser_name()));
    }

    @Order(3)
    @DisplayName("3.Verify the playlist's info match")
    @Test
    public void getInf() {
        getInfoValidation(response, data.getPlayList().isCollaborative(), data.getPlayList().getDescription(), data.getPlayList().getName());
    }

    @Order(4)
    @DisplayName("4.Verify playlist exist under all playlists")
    @Test
    public void playlistExistTest() {
        List<String> names = get("me/playlists").extract().path("items.name");
        assertThat(names, hasItem(data.getPlayList().getName()));
    }


    @Order(5)
    @DisplayName("5.Update the playlist")
    @Test
    public void updatePlaylist() {
        String lstId = data.getPlayList().getId();
        data.setPlayList("API Test Updated", "This is the updated list", false);
        put("/playlists/" + lstId, data.getPlayList());

    }

    @Order(6)
    @DisplayName("6.Retrieve the playlist's info")
    @Test
    public void retrieveThePlayList() {
        response = get("playlists/" + data.getPlaylistId());
        data.setPlayList(response.extract().as(PlayList.class));
    }

    @Order(7)
    @DisplayName("7. Verify playlist is updated")
    @Test
    public void verifyTheUpdatedPlaylist() {
        getInfoValidation(response, data.getPlayList().isCollaborative(), data.getPlayList().getDescription(), data.getPlayList().getName());
    }

    @Order(8)
    @DisplayName("8. Add items to the playlist")
    @Test
    public void addItems() {
        String endpoint = String.format("playlists/%s/tracks", data.getPlaylistId());
        List<String> tracks = CsvUtil.getTextAsList("track_ids");
        post(endpoint, playlistItemsPayload(tracks));
    }

    @Order(9)
    @DisplayName("9.Capture the items ids")
    @Test
    public void captureId() {
        response = get("playlists/"+data.getPlaylistId());
        data.setItems(response.extract().jsonPath().getList("tracks.items.track", Items.class));
        List<String>ids = new ArrayList<>();
        data.getItems().forEach(each->ids.add(each.getId()));
        data.setItemIds(ids);
    }

    @Order(10)
    @DisplayName("10.Verify item ids exist")
    @Test
    public void verifyItems(){
        data.getItems().forEach(each->assertThat(data.getItemIds(),hasItem(each.getId())));
    }

    @Order(11)
    @DisplayName("11.Reorder items in the play list")
    @Test
    public void reOrderItems(){
        String lstId=data.getPlaylistId();
        Map<String,Integer> payload = reorderPayload(1,0);
        put("/playlists/"+lstId+"/tracks",payload);
    }

    @Order(12)
    @DisplayName("11.verify order is replaced")
    @Test
    public void orderVerification(){
        response = get("playlists/"+data.getPlaylistId());
        data.setItems(response.extract().jsonPath().getList("tracks.items.track", Items.class));
        List<String>capturedIds = new ArrayList<>();
        data.getItems().forEach(each->capturedIds.add(each.getId()));
        Collections.reverse(capturedIds);
        assertThat(capturedIds,is(data.getItemIds()));
    }

    @Order(13)
    @DisplayName("12.Delete all the items")
    @Test
    public void deleteItems(){
        List<String> itemIds = CsvUtil.getTextAsList("track_ids");
        JSONObject wholeJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        itemIds.forEach(each->jsonArray.put(new JSONObject().put("uri",each)));
        wholeJson.put("tracks",jsonArray);
        String str = wholeJson.toString();
        RestAssured.given().spec(givenSpec()).body(str).when().delete(String.format("/playlists/%s/tracks",data.getPlaylistId()));
    }
}
