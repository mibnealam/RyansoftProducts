package com.example.mahfuj.ryansoftproducts;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.mahfuj.ryansoftproducts.MainActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving product data from database.
 */
public final class QueryUtils {

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Product JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Product} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Product> extractFeatureFromJson(String productJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(productJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Products to
        List<Product> products = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(productJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or Products).
            JSONArray productArray = baseJsonResponse.getJSONArray("server_response");

            // For each Product in the ProductArray, create an {@link Product} object
            for (int i = 0; i < productArray.length(); i++) {

                // Get a single Product at position i within the list of Products
                JSONObject currentProduct = productArray.getJSONObject(i);

                // For a given Product, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that Product.
                //JSONObject properties = currentProduct.getJSONObject("properties");

                // Extract the value for the key called "product_id"
                int id = Integer.parseInt(currentProduct.getString("product_id"));

                // Extract the value for the key called "product_name"
                String name = currentProduct.getString("product_name");

                // Extract the value for the key called "product_type"
                String type = currentProduct.getString("product_type");

                // Extract the value for the key called "unit_price"
                int price = Integer.parseInt(currentProduct.getString("unit_price"));

                // Create a new {@link Product} object with the id, name, type,
                // and price from the JSON response.
                Product product = new Product(id, name, type, price);

                // Add the new {@link Product} to the list of Products.
                products.add(product);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Product JSON results", e);
        }
        // Return the list of Products
        return products;
    }

    /**
     * Query the database table and return a list of {@link Product} objects.
     */
    public static List<Product> fetchProductData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Product}s
        List<Product> products = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Product}s
        return products;
    }
}
