package com.teamabalone.abalone.Helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

public class Helpers {
    public static Drawable TextureToDrawable(Texture texture) {
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public static Gson GetGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingStrategy(new FieldNamingStrategy() {

            @Override
            public String translateName(Field field) {
                return LowerCaseConverter(field.getName());  // write custom logic here
            }
        });

        return builder.create();
    }

    private static String LowerCaseConverter(String fieldName) {
        return fieldName == null || fieldName.isEmpty() ? "" : Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
    }
}
