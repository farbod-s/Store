package com.example.shopping.app.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.shopping.R;
import com.example.shopping.api.model.WC_ProductReviews;
import com.example.shopping.api.model.WC_Product;
import com.example.shopping.api.model.WC_ProductTemp;
import com.example.shopping.app.AppController;
import com.example.shopping.app.utility.Utils;
import com.example.shopping.app.adapter.ProductHorizontalAdapter;
import com.example.shopping.app.adapter.ReviewListAdapter;
import com.example.shopping.app.model.Attribute;
import com.example.shopping.app.model.Image;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.Product;
import com.example.shopping.app.model.ProductTemp;
import com.example.shopping.app.model.Review;
import com.example.shopping.app.ui.dialog.AddToCartDialog;
import com.example.shopping.app.ui.widget.ExpandableHeightListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProductActivity extends ToolbarActivity implements ObservableScrollViewCallbacks {
    @Bind(R.id.slider) SliderLayout mSliderView;
    @Bind(R.id.anchor) View mAnchor;
    @Bind(R.id.toolbar) View mToolbarView;
    @Bind(R.id.scroll) ObservableScrollView mScrollView;

    @Bind(R.id.load_progress) ProgressBar mLoadProgress;
    @Bind(R.id.load_error_layout) View mLoadErrorView;

    @Bind(R.id.product_content_layout) View mProductContentView;
    @Bind(R.id.product_title) TextView mProductName;
    @Bind(R.id.product_price) TextView mProductPrice;
    @Bind(R.id.product_discount) TextView mProductDiscount;
    @Bind(R.id.product_short_description) TextView mProductShortDescription;

    @Bind(R.id.comment_load_progress) ProgressBar mReviewsProgress;
    @Bind(R.id.comment_load_error_layout) View mReviewsErrorLayout;
    @Bind(R.id.comment_empty_message) TextView mReviewsEmptyMessage;
    @Bind(R.id.comment_show_more_link) TextView mReviewsMoreLink;
    @Bind(R.id.reviews) ExpandableHeightListView mReviewsListView;

    @Bind(R.id.related_products_load_progress) ProgressBar mRelatedProductsProgress;
    @Bind(R.id.related_products_load_error_layout) View mRelatedProductsErrorLayout;
    @Bind(R.id.related_products_empty_message) TextView mRelatedProductsEmptyMessage;
    @Bind(R.id.related_product_list) RecyclerView mRelatedProductList;

    @Bind(R.id.add_item_to_cart_button) Button mAddToCartButton;

    private int mParallaxImageHeight;

    private int mProductId;
    private boolean mReviewsAllowed;
    private Map<Integer, Boolean> mRelatedProductsTracker;

    private List<Integer> mRelatedIds;

    private List<Review> mReviews;
    private ReviewListAdapter mReviewAdapter;

    private List<Attribute> mAttributes;

    private ProductHorizontalAdapter mRelatedProductAdapter;
    private LinearLayoutManager mRelatedProductLayoutManager;

    private Product mProduct = null;

    private AddToCartDialog mAddToCartDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // bind views
        bindViews();

        // update toolbar
        updateToolbarTitle(R.string.activity_product_title);
        showToolbarBackButton();
        updateToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        // init
        mProductId = getIntent().getIntExtra(Utils.INTENT_PRODUCT_KEY, -1);
        mReviewsAllowed = false;
        mRelatedProductsTracker = new HashMap<>();
        mRelatedIds = new ArrayList<>();

        // prepare scroll view
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = Utils.getDisplayWidth(this); //getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        // preprocess
        mLoadProgress.setVisibility(View.VISIBLE);

        mAttributes = new ArrayList<>();

        // reviews
        mReviews = new ArrayList<>();
        mReviewAdapter = new ReviewListAdapter(this, mReviews);
        mReviewsListView.setExpanded(true);
        mReviewsListView.setAdapter(mReviewAdapter);

        // related products
        mRelatedProductLayoutManager = new LinearLayoutManager(this);
        mRelatedProductLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRelatedProductList.setLayoutManager(mRelatedProductLayoutManager);
        mRelatedProductList.setItemAnimator(new DefaultItemAnimator());
        mRelatedProductList.setHasFixedSize(true);
        mRelatedProductAdapter = new ProductHorizontalAdapter();
        mRelatedProductList.setAdapter(mRelatedProductAdapter);

        // get Product info
        getProduct();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mSliderView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
        // nothing
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        // nothing
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSliderView.startAutoCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSliderView.stopAutoCycle();

        super.onStop();
    }

    @OnClick(R.id.add_item_to_cart_button)
    public void onAddToCartClicked() {
        if (mAddToCartDialog == null) {
            mAddToCartDialog = new AddToCartDialog(this, mAttributes);
        }
        mAddToCartDialog.show();
    }

    @OnClick(R.id.product_full_detail_trigger)
    public void onProductMoreDetailClicked() {
        // TODO - show more detail activity
    }

    @OnClick(R.id.comment_show_more_link)
    public void showMoreReview() {
        Intent intent = new Intent(ProductActivity.this, ReviewListActivity.class);
        intent.putExtra(Utils.INTENT_PRODUCT_KEY, mProductId);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Subscribe
    public void onProductClicked(ProductTemp product) {
        Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
        intent.putExtra(Utils.INTENT_PRODUCT_KEY, product.id);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Subscribe
    public void onShoppingItemAdded(Item item) {
        // add item to shopping cart
        item.product_id = mProductId;
        item.name = mProduct.title;
        item.price = String.valueOf(mProduct.price);
        item.featured_src = mProduct.featured_src;
        AppController.getInstance().getShoppingCart().addItem(item);

        // update shopping cart badge
        int count = AppController.getInstance().getShoppingCart().getItemsCount();
        if (count > 0) {
            updateShoppingCartBadge(count);
            setShoppingCartBadgeVisibility(true);
        } else {
            setShoppingCartBadgeVisibility(false);
        }
    }

    @OnClick(R.id.load_retry_button)
    public void retryGetProduct() {
        // preprocess
        mLoadErrorView.setVisibility(View.GONE);
        mLoadProgress.setVisibility(View.VISIBLE);

        getProduct();
    }

    @OnClick(R.id.comment_load_retry_button)
    public void retryLoadReviews() {
        // preprocess
        mReviewsErrorLayout.setVisibility(View.GONE);

        setupRecentReviews(mProductId);
    }

    @OnClick(R.id.related_products_load_retry_button)
    public void retryLoadRelatedProducts() {
        // preprocess
        mRelatedProductsErrorLayout.setVisibility(View.GONE);

        setupRelatedProducts(mRelatedIds);
    }

    private void getProduct() {
        AppController.getInstance().getRestClient().getApiService().getProduct(mProductId,
                Product.fields(),
                new Callback<WC_Product>() {
                    @Override
                    public void success(WC_Product product, Response response) {
                        // setup slider & product info & reviews & related products
                        mReviewsAllowed = product.product.reviews_allowed;
                        mRelatedIds = product.product.related_ids;

                        setupSlider(product.product.images);
                        setupProduct(product.product);
                        setupRecentReviews(product.product.id);
                        setupRelatedProducts(mRelatedIds);

                        // postprocess
                        mLoadProgress.setVisibility(View.GONE);
                        mSliderView.setVisibility(View.VISIBLE);
                        mAnchor.setVisibility(View.VISIBLE);
                        mProductContentView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // postprocess
                        mLoadProgress.setVisibility(View.GONE);
                        mLoadErrorView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setupSlider(List<Image> images) {
        if (images != null && images.size() > 0) {
            // init
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    mParallaxImageHeight);
            mSliderView.setLayoutParams(layoutParams);
            mAnchor.setLayoutParams(layoutParams);
            mAnchor.setMinimumHeight(mParallaxImageHeight);

            mSliderView.setPresetTransformer(SliderLayout.Transformer.Default);
            mSliderView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderView.setDuration(5000);

            // create slider show
            for (Image image : images) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description(getResources().getString(R.string.empty) /*image.title*/)
                .image(image.src)
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderView.addSlider(textSliderView);
            }
        }
    }

    private void setupProduct(Product product) {
        mProduct = product;

        mProductName.setText(product.title);
        mProductPrice.setText(String.valueOf(product.regular_price));
        mProductDiscount.setText(String.valueOf(product.sale_price));
        if (product.on_sale) {
            mProductPrice.setPaintFlags(mProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mProductDiscount.setVisibility(View.VISIBLE);
        } else {
            mProductDiscount.setVisibility(View.GONE);
            mProductPrice.setPaintFlags(mProductPrice.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        // TODO - show total sales or other info and config share button
        mProductShortDescription.setText(product.short_description);
        mAttributes.addAll(product.attributes);
        mAddToCartButton.setVisibility(View.VISIBLE);
    }

    private void setupRecentReviews(int productId) {
        if (productId > 0) {
            // preprocess
            mReviewsProgress.setVisibility(View.VISIBLE);

            AppController.getInstance().getRestClient().getApiService().getProductRecentReviewList(
                    productId,
                    3,
                    new Callback<WC_ProductReviews>() {
                        @Override
                        public void success(WC_ProductReviews productReviews, Response response) {
                            // postprocess
                            mReviewsProgress.setVisibility(View.GONE);
                            if (productReviews.product_reviews.size() > 0) {
                                mReviews.addAll(productReviews.product_reviews);
                                mReviewAdapter.notifyDataSetChanged();

                                mReviewsListView.setVisibility(View.VISIBLE);

                                if (productReviews.product_reviews.size() == 3) {
                                    mReviewsMoreLink.setVisibility(View.VISIBLE);
                                }
                            } else {
                                mReviewsEmptyMessage.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            // postprocess
                            mReviewsProgress.setVisibility(View.GONE);
                            mReviewsErrorLayout.setVisibility(View.VISIBLE);
                        }
                    }
            );
        }
    }

    private void setupRelatedProducts(final List<Integer> productRelatedIds) {
        if (productRelatedIds != null && productRelatedIds.size() > 0) {
            // preprocess
            mRelatedProductsProgress.setVisibility(View.VISIBLE);
            mRelatedProductsTracker.clear();

            for (final Integer productId : productRelatedIds) {
                AppController.getInstance().getRestClient().getApiService().getProductTemp(
                        productId,
                        ProductTemp.fields(),
                        new Callback<WC_ProductTemp>() {
                            @Override
                            public void success(WC_ProductTemp product, Response response) {
                                mRelatedProductAdapter.add(mRelatedProductAdapter.getItemCount(), product.product);

                                mRelatedProductsTracker.put(productId, true);
                                if (mRelatedProductsTracker.size() == productRelatedIds.size()) {
                                    for (Integer productId : mRelatedProductsTracker.keySet()) {
                                        if (mRelatedProductsTracker.get(productId)) { // success
                                            mRelatedIds.remove(productId);
                                        }
                                    }
                                    if (mRelatedIds.isEmpty()) {
                                        // postprocess
                                        mRelatedProductsProgress.setVisibility(View.GONE);
                                        mRelatedProductList.setVisibility(View.VISIBLE);
                                    } else {
                                        mRelatedProductsProgress.setVisibility(View.GONE);
                                        mRelatedProductsErrorLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                mRelatedProductsTracker.put(productId, false);
                                if (mRelatedProductsTracker.size() == productRelatedIds.size()) {
                                    for (Integer productId : mRelatedProductsTracker.keySet()) {
                                        if (mRelatedProductsTracker.get(productId)) { // success
                                            mRelatedIds.remove(productId);
                                        }
                                    }
                                    mRelatedProductsProgress.setVisibility(View.GONE);
                                    mRelatedProductsErrorLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                );
            }
        } else {
            mRelatedProductsEmptyMessage.setVisibility(View.VISIBLE);
        }
    }
}