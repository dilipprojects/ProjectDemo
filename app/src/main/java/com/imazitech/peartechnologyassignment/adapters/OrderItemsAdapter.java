package com.imazitech.peartechnologyassignment.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.imazitech.peartechnologyassignment.R;
import com.imazitech.peartechnologyassignment.models.CartItems;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {

    private List<CartItems> cartItemsList = new ArrayList<>();
    private Context context;

    public OrderItemsAdapter(List<CartItems> cartItemsList, Context context) {
        this.cartItemsList = cartItemsList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return cartItemsList !=null ? cartItemsList.size() : 0;
    }


    @Override
    public void onBindViewHolder(final OrderItemViewHolder holder, final int position) {

        final CartItems model = cartItemsList.get(position);

        holder.tv_item_name.setText(!TextUtils.isEmpty(model.getName()) ? model.getName() : "");

        if(!TextUtils.isEmpty(model.getCustomisation().trim())){
            holder.tv_customization.setVisibility(View.VISIBLE);
            holder.tv_customization.setText("Customization: "+model.getCustomisation());
        }else {
            holder.tv_customization.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(model.getInstructions().trim())){
            holder.tv_cooking_instruction.setVisibility(View.VISIBLE);
            holder.tv_cooking_instruction.setText("Cooking Instructions: "+model.getInstructions());
        }else {
            holder.tv_cooking_instruction.setVisibility(View.GONE);
        }

        /* if(model.getOptionPrice() != 0){
            holder.tv_total_amount.setPaintFlags( holder.tv_total_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_total_amount.setVisibility(View.VISIBLE);
            holder.tv_total_amount.setText(context.getString(R.string.Rs)+" "+model.getOptionPrice());
        }else {
            holder.tv_total_amount.setVisibility(View.GONE);
        }*/

       /* holder.tv_total_amount.setText(context.getString(R.string.Rs)+ model.getPrice());
        holder.tv_total_amount.setPaintFlags( holder.tv_total_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_total_amount.setVisibility(View.VISIBLE);*/

        if(!TextUtils.isEmpty(model.getItemType())){
            if(model.getItemType().equalsIgnoreCase("1")){
                holder.img_vag.setVisibility(View.VISIBLE);
                holder.img_non_veg.setVisibility(View.GONE);
            }else {
                holder.img_non_veg.setVisibility(View.VISIBLE);
                holder.img_vag.setVisibility(View.GONE);
            }
        }

        if(model.getOptionPrice() !=0){
            int totalPrice = model.getPrice() + model.getOptionPrice();
            holder.tv_total_payable_amount.setText(!TextUtils.isEmpty(String.valueOf(totalPrice)) ? context.getString(R.string.Rs)+" "+totalPrice : context.getString(R.string.Rs)+" 0");
        }else {
            holder.tv_total_payable_amount.setText(!TextUtils.isEmpty(model.getPrice().toString()) ? context.getString(R.string.Rs)+" "+model.getPrice().toString() : context.getString(R.string.Rs)+" 0");
        }

        holder.tv_qty.setText(!TextUtils.isEmpty(model.getQuantity().toString()) ? "Qty: "+model.getQuantity().toString() : "Qty: 1");

    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img_vag,img_non_veg;
        TextView tv_item_name,tv_total_amount,tv_total_payable_amount,tv_customization,tv_cooking_instruction,tv_qty;

        private OrderItemViewHolder(View view) {
            super(view);
            img_vag = view.findViewById(R.id.img_vag);
            img_non_veg = view.findViewById(R.id.img_non_veg);
            tv_item_name = view.findViewById(R.id.tv_item_name);
            tv_total_amount = view.findViewById(R.id.tv_total_amount);
            tv_total_payable_amount = view.findViewById(R.id.tv_total_payable_amount);
            tv_customization = view.findViewById(R.id.tv_customization);
            tv_cooking_instruction = view.findViewById(R.id.tv_cooking_instruction);
            tv_qty = view.findViewById(R.id.tv_qty);
        }
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view, parent, false);
        return new OrderItemViewHolder(itemView);
    }
}

