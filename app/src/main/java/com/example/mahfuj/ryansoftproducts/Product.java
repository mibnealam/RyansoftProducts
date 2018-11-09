package com.example.mahfuj.ryansoftproducts;

public class Product {

    /* ID of the product */
    private int productId;

    /* Name of the product */
    private String productName;

    /* Type of the product */
    private String productType;

    /* Price of the product */
    private int productPrice;

    public Product(){
    }
    /**Constructs a new {@link Product} object.
     *
     * @param productId is the id of the product
     * @param productName is the name of the product
     * @param productPrice is the unit price of the product
     */
    public Product (int productId, String productName, String productType, int productPrice){
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.productPrice = productPrice;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
