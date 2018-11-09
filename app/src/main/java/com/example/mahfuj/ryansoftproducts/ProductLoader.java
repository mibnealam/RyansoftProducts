package com.example.mahfuj.ryansoftproducts;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
/**
 * Loads a list of products by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ProductLoader extends AsyncTaskLoader<List<Product>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ProductLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link ProductLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ProductLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Nullable
    @Override
    public List<Product> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of products.
        List<Product> products = QueryUtils.fetchProductData(mUrl);
        return products;
    }
}
