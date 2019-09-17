package com.ingic.caristocrat.helpers;

import com.google.gson.Gson;

import java.util.ArrayList;

public class JsonHelpers {
    public static Object convertToModelClass(Object response, Class modelClas) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(response).toString(),modelClas);
    }

    public static ArrayList<Object> convertToArrayModelClass(ArrayList<Object> response, Class modelClas) {
        Gson gson = new Gson();
        ArrayList<Object> list = new ArrayList<>();
        for (int index = 0; index < response.size(); index++) {
            list.add(gson.fromJson(gson.toJson(response.get(index)).toString(),modelClas));
        }
        return list;
    }
}
