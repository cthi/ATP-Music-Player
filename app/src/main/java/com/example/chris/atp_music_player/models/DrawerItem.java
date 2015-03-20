package com.example.chris.atp_music_player.models;

public class DrawerItem {

    String label;
    int imgId;

    public DrawerItem(String label, int imgId){
        this.label = label;
        this.imgId = imgId;
    }

    public DrawerItem(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
