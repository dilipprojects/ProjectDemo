package com.imazitech.peartechnologyassignment;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.imazitech.peartechnologyassignment.adapters.OrderItemsAdapter;
import com.imazitech.peartechnologyassignment.models.CartBatchItems;
import com.imazitech.peartechnologyassignment.models.CartItems;
import com.imazitech.peartechnologyassignment.models.OrderDetailsResponseModel;
import com.imazitech.peartechnologyassignment.network.APIClient;
import com.imazitech.peartechnologyassignment.network.APIInterface;
import com.imazitech.peartechnologyassignment.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CircleImageView img_hotel_logo;
    ImageView img_back;
    TextView tv_hotel_name,tv_address,tv_ordered_date,tv_total_amount,tv_payment_mode,tv_total_bill,tv_total_bill_amount,tv_bill_payable_amount;
    TextView tv_subtotal_amount,tv_taxes_amount,tv_discount_amount,tv_coupon_discount,tv_total_final_bill_amount;
    RecyclerView rv_foodList;
    List<CartBatchItems> cartBatchItemsList = new ArrayList<>();
    List<CartItems> cartItemsList = new ArrayList<>();
    OrderItemsAdapter orderItemsAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViewItem();
    }

    /**
     * Initializing All View Item
     * */

    private void initializeViewItem() {

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        img_hotel_logo = findViewById(R.id.img_hotel_logo);
        tv_hotel_name = findViewById(R.id.tv_hotel_name);
        tv_address = findViewById(R.id.tv_address);
        tv_ordered_date = findViewById(R.id.tv_ordered_date);
        tv_payment_mode = findViewById(R.id.tv_payment_mode);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_total_bill = findViewById(R.id.tv_total_bill);
        tv_total_bill_amount = findViewById(R.id.tv_total_bill_amount);
        tv_bill_payable_amount = findViewById(R.id.tv_bill_payable_amount);
        tv_subtotal_amount = findViewById(R.id.tv_subtotal_amount);
        tv_taxes_amount = findViewById(R.id.tv_taxes_amount);
        tv_discount_amount = findViewById(R.id.tv_discount_amount);
        tv_coupon_discount = findViewById(R.id.tv_coupon_discount);
        tv_total_final_bill_amount = findViewById(R.id.tv_total_final_bill_amount);
        rv_foodList = findViewById(R.id.rv_foodList);

        getCartDetailsSetup();
    }

    /**
     * Checking Internet Connection Before Network Api Call
     * */

    private void getCartDetailsSetup() {
        if (Utility.internetCheck(this)) {
            getCartDetails();
        }
    }

    /**
     * getCartDetails(): -  Network Call to get All Order details From Api
     * */

    private void getCartDetails() {
        shimmerLayoutVisibleStart();

        APIInterface apiService = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiService.getCartOrderData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null && response.code() == 200) {
                        String result = response.body().string();
                        OrderDetailsResponseModel messageResponse = new Gson().fromJson(result, OrderDetailsResponseModel.class);
                        if(messageResponse !=null){
                            setDataInView(messageResponse);
                        }else {
                            Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Somethings Went Wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Somethings Went Wrong", Toast.LENGTH_SHORT).show();
                }finally {
                    shimmerLayoutGoneStop();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                shimmerLayoutGoneStop();
                Toast.makeText(MainActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * After getting data from Api Setting in View and Setting in Adapter
     * */

    private void setDataInView(OrderDetailsResponseModel orderDetails) {
        Glide.with(this)
                .load(Uri.parse(orderDetails.getRestaurantImage()))
                .error(R.drawable.hotel)
                .into(img_hotel_logo);

        tv_hotel_name.setText(!TextUtils.isEmpty(orderDetails.getRestaurantName()) ? orderDetails.getRestaurantName() : "");
        tv_address.setText(!TextUtils.isEmpty(orderDetails.getRestaurantLocation()) ? orderDetails.getRestaurantLocation() : "");
        tv_ordered_date.setText(!TextUtils.isEmpty(orderDetails.getTimestamp()) ? "Ordered On: "+orderDetails.getTimestamp() : "");
        tv_payment_mode.setText(!TextUtils.isEmpty(orderDetails.getPaymentType()) ? "Payment Mode : "+ orderDetails.getPaymentType() : "");
        tv_total_amount.setText(!TextUtils.isEmpty(orderDetails.getGrandTotal().toString()) ? getText(R.string.Rs) + " "+ orderDetails.getGrandTotal().toString() : "");

        tv_total_bill_amount.setText(!TextUtils.isEmpty(orderDetails.getSubTotal().toString()) ? getText(R.string.Rs) + " "+ orderDetails.getSubTotal().toString() : getText(R.string.Rs) + " 0");
        tv_total_bill_amount.setPaintFlags(tv_total_bill_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_bill_payable_amount.setText(!TextUtils.isEmpty(orderDetails.getGrandTotal().toString()) ? getText(R.string.Rs) + " "+ orderDetails.getGrandTotal().toString() : "");

        tv_subtotal_amount.setText(!TextUtils.isEmpty(orderDetails.getSubTotal().toString()) ? getText(R.string.Rs) + " "+ orderDetails.getSubTotal().toString() : getText(R.string.Rs) + " 0");

        String totalTaxes = String.valueOf(orderDetails.getCgst() + orderDetails.getSgst());

        tv_taxes_amount.setText(!TextUtils.isEmpty(totalTaxes) ? getText(R.string.Rs) + " "+ totalTaxes : getText(R.string.Rs) + " 0");
        tv_discount_amount.setText(!TextUtils.isEmpty(orderDetails.getTotalDiscount().toString()) ? "- " +getText(R.string.Rs) + " "+ orderDetails.getTotalDiscount().toString() : getText(R.string.Rs) + " 0");
        tv_coupon_discount.setText(!TextUtils.isEmpty(orderDetails.getPeARDiscount().toString()) ? "- " + getText(R.string.Rs) + " "+ orderDetails.getPeARDiscount().toString() : getText(R.string.Rs) + " 0");
        tv_total_final_bill_amount.setText(!TextUtils.isEmpty(orderDetails.getGrandTotal().toString()) ? getText(R.string.Rs) + " "+ orderDetails.getGrandTotal().toString() : getText(R.string.Rs) + " 0");

        cartBatchItemsList = orderDetails.getBatch();

        for (CartBatchItems cartBatchItems : cartBatchItemsList){
            cartItemsList.addAll(cartBatchItems.getItems());
        }

        if(cartItemsList.size() !=0){
            setOrderAdapter(cartItemsList);
        }
    }

    private void setOrderAdapter(List<CartItems> cartItemsList) {
        orderItemsAdapter = new OrderItemsAdapter(cartItemsList,MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        rv_foodList.setLayoutManager(linearLayoutManager);
        rv_foodList.setItemAnimator(new DefaultItemAnimator());
        rv_foodList.setAdapter(orderItemsAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private void shimmerLayoutVisibleStart(){
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
    }

    private void shimmerLayoutGoneStop(){
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }
}