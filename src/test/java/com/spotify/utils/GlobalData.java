package com.spotify.utils;

import com.spotify.pojos.Items;
import com.spotify.pojos.PlayList;
import lombok.Data;

import java.util.List;

@Data
public class GlobalData {

    private static final String user_id = ConfigurationReader.getProperty("user_id");
    private PlayList playList;
    private String playlistId;
    private List<Items> items;
    private List<String> itemIds;

    public String getUser_id() {
        return user_id;
    }
    public void setPlayList(String name,String description,boolean collaborative) {
       PlayList playList = new PlayList();
       playList.setName(name);
       playList.setDescription(description);
       playList.setCollaborative(collaborative);
       this.playList= playList;
    }
}
