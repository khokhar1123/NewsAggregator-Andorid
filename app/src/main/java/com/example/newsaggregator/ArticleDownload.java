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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArticleDownload {
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    //    private final String source;
    final static String SURL = "https://newsapi.org/v2/top-headlines";
    final static String APIKEY = "c961ec7f38c24a66a8056d454c548bd6";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadArt(MainActivity mainActivityIn, String source) {
        Uri.Builder buildURL = Uri.parse(SURL).buildUpon();
        buildURL.appendQueryParameter("sources", source);
        buildURL.appendQueryParameter("apiKey", APIKEY);
        String formatedURL = buildURL.build().toString();
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);
        Response.Listener<JSONObject> listener =
                response -> mainActivity.updateArticle((parseArticle(response.toString())));
        Response.ErrorListener error =
                error1 -> mainActivity.downloadFailed();
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, formatedURL, null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static ArrayList<Article> parseArticle(String s) {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(s);
            JSONArray articlesResponse = response.getJSONArray("articles");
            for (int i = 0; i < articlesResponse.length(); i++) {
                JSONObject individualArticle = (JSONObject) articlesResponse.get(i);
                String author = individualArticle.getString("author");
                String title = individualArticle.getString("title");
                String description = individualArticle.getString("description");
                String url = individualArticle.getString("url");
                String urlToImage = individualArticle.getString("urlToImage");
                String rawPub = individualArticle.getString("publishedAt");
                DateTimeFormatter dtfParse = DateTimeFormatter.ISO_DATE_TIME;
                Instant ins = dtfParse.parse(rawPub, Instant::from);
                LocalDateTime ldt = LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, yyyy hh:mm a");
                String finPub=ldt.format(dtf);

                if (author.equals("null")) {
                    author = "";
                }
                if (title.equals("null")) {
                    title = "";
                }
                if (description.equals("null")) {
                    description = "";
                }
                if (finPub.isEmpty()) {
                    finPub = "";
                }
                if (author.contains("[")) {
                    String sub = author.substring(27);
                    String[] split = sub.split("\"");
                    author = split[0];
                }
                if (title.contains("<")) {
                    title = title.replaceAll("<*>", "");
                }
                Article toAdd = new Article(author, title, description, url, urlToImage,
                        finPub);
                articles.add(toAdd);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return articles;
    }

}
