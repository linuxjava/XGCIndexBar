package com.example;

import com.google.gson.annotations.SerializedName;

public class SData {
    public static final int ITEM_TYPE_SECTION = 1;
    public static final int ITEM_TYPE_STATION = 2;

    @SerializedName("type")
    public int itemType;
    @SerializedName("pys")
    public String section;
    @SerializedName("name")
    public String stationName;
}
