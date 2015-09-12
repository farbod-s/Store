package com.example.shopping.api;

import com.example.shopping.api.model.WC_Authenticate;
import com.example.shopping.api.model.WC_Coupon;
import com.example.shopping.api.model.WC_Customer;
import com.example.shopping.api.model.WC_CustomerOrders;
import com.example.shopping.api.model.WC_Message;
import com.example.shopping.api.model.WC_Order;
import com.example.shopping.api.model.WC_ProductCategories;
import com.example.shopping.api.model.WC_ProductReviews;
import com.example.shopping.api.model.WC_ProductTemp;
import com.example.shopping.api.model.WC_Products;
import com.example.shopping.api.model.WC_Product;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;


public interface WoocommerceService {

    @GET("/wc-api/v2/products?filter[order]=DESC&filter[orderby]=meta_value&filter[orderby_meta_key]=total_sales")
    public void getPopularProductList(@Query("fields") String fields, @Query("page") int page, Callback<WC_Products> callback);

    @GET("/wc-api/v2/products")
    public void getProductList(@Query("fields") String fields, @Query("page") int page, Callback<WC_Products> callback);

    @GET("/wc-api/v2/products/categories")
    public void getCategoryList(Callback<WC_ProductCategories> callback);

    @GET("/wc-api/v2/products")
    public void search(@Query("filter[q]") String searchQuery, @Query("fields") String fields, @Query("page") int page, Callback<WC_Products> callback);

    @GET("/wc-api/v2/products")
    public void getCategoryProductList(@Query("filter[category]") String categoryName, @Query("fields") String fields, @Query("page") int page, Callback<WC_Products> callback);

    @GET("/wc-api/v2/products/{id}")
    public void getProduct(@Path("id") int productId, @Query("fields") String fields, Callback<WC_Product> callback);

    @GET("/wc-api/v2/products/{id}")
    public void getProductTemp(@Path("id") int productId, @Query("fields") String fields, Callback<WC_ProductTemp> callback);

    @GET("/wc-api/v2/products/{id}/reviews") // TODO add pagination
    public void getProductReviewList(@Path("id") int productId, Callback<WC_ProductReviews> callback);

    @GET("/wc-api/v2/products/{id}/reviews")
    public void getProductRecentReviewList(@Path("id") int productId, @Query("filter[limit]") int limit, Callback<WC_ProductReviews> callback);

    @GET("/wc-api/v2/customers/{id}") // profile
    public void getCustomer(@Path("id") int customerId, Callback<WC_Customer> callback);

    @POST("/wc-api/v2/customers/authenticate") // sign in
    public void authenticate(@Body WC_Authenticate authenticationData, Callback<WC_Customer> callback);

    @POST("/wc-api/v2/customers") // sign up
    public void createCustomer(@Body WC_Customer newCustomer, Callback<WC_Customer> callback);

    @GET("/wc-api/v2/coupons/code/{code}")
    public void getCoupon(@Path("code") String code, Callback<WC_Coupon> callback);

    @GET("/wc-api/v2/customers/{id}/orders") // TODO add pagination
    public void getCustomerOrderList(@Path("id") int customerId, @Query("fields") String fields, Callback<WC_CustomerOrders> callback);

    @GET("/wc-api/v2/orders/{id}")
    public void getOrder(@Path("id") int orderId, Callback<WC_Order> callback);

    @POST("/wc-api/v2/orders")
    public void createOrder(@Body WC_Order newOrder, Callback<WC_Order> callback);

    @POST("/wc-api/v2/orders/{id}")
    public void deleteOrder(@Path("id") int orderId, Callback<WC_Message> callback);

    @PUT("/wc-api/v2/orders/{id}")
    public void updateOrder(@Path("id") int orderId, @Body WC_Order order, Callback<WC_Order> callback);

    // TODO - checkout shopping cart (payment)
}
