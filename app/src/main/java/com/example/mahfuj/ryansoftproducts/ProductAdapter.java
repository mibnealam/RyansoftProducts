package com.example.mahfuj.ryansoftproducts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> implements Filterable {

    Context context;
    private List<Product> productList;
    private List<Product> productListFiltered;
    //private ProductsAdapterListener listener;

    public ProductAdapter(){
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ProductAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_product;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ProductAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the product
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param productAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(final ProductAdapterViewHolder productAdapterViewHolder, int position) {

        //Initialization and setting the product data into views.
        final Product product = productListFiltered.get(position);
        productAdapterViewHolder.mProductIdTextView.setText(String.valueOf(product.getProductId()));
        productAdapterViewHolder.mProductNameTextView.setText(product.getProductName());
        productAdapterViewHolder.mProductTypeTextView.setText(product.getProductType());
        productAdapterViewHolder.mUnitPriceTextView.setText(String.valueOf(product.getProductPrice()));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our course list
     */

    @Override
    public int getItemCount() {
        if ( null == productListFiltered) return 0;
        return productListFiltered.size();
    }

    /**
     * This method is used to set the product data on a ProductAdapter if we've already
     * created one. This is handy when we get new data from the database but don't want to create a
     * new ProductAdapter to display it.
     *
     * @param productData The new product data to be displayed.
     */
    public void setProductData(List<Product> productData) {
        productList = productData;
        productListFiltered = productList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())
                                || row.getProductType().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    /**
     * Cache of the children views for a product list item.
     */
    public class ProductAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mProductIdTextView;
        public final TextView mProductNameTextView;
        public final TextView mProductTypeTextView;
        public final TextView mUnitPriceTextView;

        public ProductAdapterViewHolder(View itemView) {
            super(itemView);
            mProductIdTextView = (TextView) itemView.findViewById(R.id.product_id);
            mProductNameTextView = (TextView) itemView.findViewById(R.id.product_name);
            mProductTypeTextView = (TextView) itemView.findViewById(R.id.product_Type);
            mUnitPriceTextView = (TextView) itemView.findViewById(R.id.unit_price);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected product in callback
//                    listener.onProductSelected(productListFiltered.get(getAdapterPosition()));
//                }
//            });
        }
    }

//    public interface ProductsAdapterListener {
//        void onProductSelected(Product product);
//    }
}
