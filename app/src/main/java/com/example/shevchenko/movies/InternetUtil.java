package com.example.shevchenko.movies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class with help functions for internet requests
 */
class InternetUtil {
    private static final String API_KEY_PARAM = "api_key";

    private static Uri addApiKey(Uri builder) {
        return builder.buildUpon()
                // Using class to hold API Key and not submitting it to public repo
                // To run this app replace ApiKey.getApiKey() call with your API key
                .appendQueryParameter(API_KEY_PARAM, ApiKey.getApiKey())
                .build();
    }

    /**
     * Adds API Key parameter to given URL, makes GET request to new URL (with API key added).
     * Returns JSON response as String.
     */
    public static String getJsonResponseAsString(Uri builder, String debugTag) {
        String response = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(addApiKey(builder).toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                return null;
            }
            response = buffer.toString();
        } catch (Exception e) {
            Log.e(debugTag, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(debugTag, "Error closing stream", e);
                }
            }
        }
        return response;
    }
}
