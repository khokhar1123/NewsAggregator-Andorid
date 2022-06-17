package com.example.newsaggregator;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class SourceDownload {
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    final static String SURL = "https://newsapi.org/v2/sources";
    final static String APIKEY = "c961ec7f38c24a66a8056d454c548bd6";
    private final static HashMap<String, HashSet<Source>> topics = new HashMap<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadSource(MainActivity mainActivityIn) {
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);
        Uri.Builder buildURL = Uri.parse(SURL).buildUpon();
        buildURL.appendQueryParameter("apiKey", APIKEY);
        String formatedURL = buildURL.build().toString();
        Response.Listener<JSONObject> listener =
                response -> mainActivity.updateSources((parseSource(response.toString())),topics);
        //        response  ->parseJSON(response.toString());
        Response.ErrorListener error =
                error1 -> mainActivity.downloadFailed();
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, formatedURL,null,listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };
        queue.add(jsonObjectRequest);
    }
    private static ArrayList<Source> parseSource(String s) {
        ArrayList<Source> sList = new ArrayList<>();
        topics.put("all", new HashSet<>());
        try {
            JSONObject response = new JSONObject(s);
            JSONArray sourcesResponse = response.getJSONArray("sources");
            for (int i = 0; i < sourcesResponse.length(); i++) {
                JSONObject individualSource = (JSONObject) sourcesResponse.get(i);
                String id = individualSource.getString("id");
                String name = individualSource.getString("name");
                String category = individualSource.getString("category");
                Source sourceD = new Source(id, name, category);
                sList.add(sourceD);
                if (!topics.containsKey(category)) {
                    topics.put(category, new HashSet<>());
                }
                Objects.requireNonNull(topics.get(category)).add(sourceD);
                Objects.requireNonNull(topics.get("all")).add(sourceD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sList;
    }

}
