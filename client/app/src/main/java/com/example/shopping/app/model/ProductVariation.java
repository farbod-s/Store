package com.example.shopping.app.model;

import java.util.List;


public class ProductVariation {
    public int id;
    public String created_at;
    public String updated_at;
    public boolean downloadable;
    public boolean virtual;
    public String permalink;
    public String sku;
    public float price;
    public float regular_price;
    public float sale_price;
    public boolean taxable;
    public String tax_status;
    public String tax_class;
    public boolean managing_stock;
    public int stock_quantity;
    public boolean in_stock;
    public boolean backordered;
    public boolean purchaseable;
    public boolean visible;
    public boolean on_sale;
    public String weight;
    public Dimension dimensions;
    public String shipping_class;
    public int shipping_class_id;
    public List<Image> images;
    public List<Attribute> attributes;
    public List<Download> downloads;
    public int download_limit;
    public int download_expiry;

    public ProductVariation() {
        // nothing!
    }

    public ProductVariation(int id, String created_at, String updated_at, boolean downloadable, boolean virtual, String permalink, String sku, float price, float regular_price, float sale_price, boolean taxable, String tax_status, String tax_class, boolean managing_stock, int stock_quantity, boolean in_stock, boolean backordered, boolean purchaseable, boolean visible, boolean on_sale, String weight, Dimension dimensions, String shipping_class, int shipping_class_id, List<Image> images, List<Attribute> attributes, List<Download> downloads, int download_limit, int download_expiry) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.downloadable = downloadable;
        this.virtual = virtual;
        this.permalink = permalink;
        this.sku = sku;
        this.price = price;
        this.regular_price = regular_price;
        this.sale_price = sale_price;
        this.taxable = taxable;
        this.tax_status = tax_status;
        this.tax_class = tax_class;
        this.managing_stock = managing_stock;
        this.stock_quantity = stock_quantity;
        this.in_stock = in_stock;
        this.backordered = backordered;
        this.purchaseable = purchaseable;
        this.visible = visible;
        this.on_sale = on_sale;
        this.weight = weight;
        this.dimensions = dimensions;
        this.shipping_class = shipping_class;
        this.shipping_class_id = shipping_class_id;
        this.images = images;
        this.attributes = attributes;
        this.downloads = downloads;
        this.download_limit = download_limit;
        this.download_expiry = download_expiry;
    }
}
