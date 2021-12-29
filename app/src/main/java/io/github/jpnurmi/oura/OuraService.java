package io.github.jpnurmi.oura;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OuraService {
    private static final String SCORE_KEY = "score";
    private static final String SLEEP_KEY = "sleep";
    private static final String URL = "https://api.ouraring.com/v1/sleep?access_token=";

    public interface OnScore {
        void onScore(int score);
        void onError(Object error);
    }

    static void fetchScore(Context context, CharSequence token, OnScore listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray sleep = response.getJSONArray(SLEEP_KEY);
                    JSONObject last = sleep.getJSONObject(sleep.length() - 1);
                    listener.onScore(last.getInt(SCORE_KEY));
                } catch (JSONException e) {
                    listener.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                listener.onError(e);
            }
        });
        Volley.newRequestQueue(context).add(request);
    }
}
