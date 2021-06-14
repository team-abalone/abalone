package com.teamabalone.abalone.Helpers;

import java.util.HashMap;

public class GameConstants {
    public static final String SERVER_URL = "abaloneapi.germanywestcentral.cloudapp.azure.com";
    //public static final String SERVER_URL = "192.168.8.100";
    public static final int SERVER_PORT = 5001;
    public static final String CUSTOM_UI_JSON = "skin/StyleExport.json";
    public static final String CUSTOM_UI_ATLAS = "skin/StyleExport.atlas";

    public static final Integer[] playerNumberSelect = new Integer[] { 2, 3, 4 };

    // Key is the display name, value the file name.
    public static HashMap<String, String> MARBLE_SKINS = new HashMap<String, String>() {{
            put("ball", "ball.png");
            put("white", "ball_white.png");
            put("bowling", "bowling_ball.png");
            put("tennis", "tennis_ball.png");
    }};

    // Key is the display name, value the file name.
    public static HashMap<String, String> BOARD_SKINS = new HashMap<String, String>() {{
        put("Laminat", "Laminat.png");
        put("Sky", "Sky.png");
        put("Wooden Table", "wooden_table.png");
    }};

    public static String getBoardSkinPathPerName(String name) {
        return BOARD_SKINS.get(name);
    }

    public static String getMarbleSkinPathPerName(String name) {
        return MARBLE_SKINS.get(name);
    }
}
