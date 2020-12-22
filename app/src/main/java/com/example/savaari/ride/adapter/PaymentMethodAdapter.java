package com.example.savaari.ride.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savaari.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PaymentMethodAdapter  extends RecyclerView.Adapter<PaymentMethodAdapter.ItemViewHolder> {

    private ArrayList<PaymentMethodItem> mItemList;
    private ItemClickListener listener;

    public PaymentMethodAdapter(ArrayList<PaymentMethodItem> itemList, ItemClickListener listener) {
        mItemList = itemList;
        this.listener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeakReference<ItemClickListener> listenerRef;
        public LinearLayout paymentConfig;
        private ImageView paymentImage;
        private TextView paymentText;

        public ItemViewHolder(@NonNull View itemView, final ItemClickListener listener) {
            super(itemView);

            paymentConfig = itemView.findViewById(R.id.payment_config);
            paymentImage = itemView.findViewById(R.id.payment_img);
            paymentText = itemView.findViewById(R.id.payment_txt);

            listenerRef = new WeakReference<>(listener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPaymentMethodItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public PaymentMethodAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_card, parent, false);
        return new ItemViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodAdapter.ItemViewHolder holder, int position) {

        PaymentMethodItem currentItem = mItemList.get(position);

        holder.paymentImage.setImageResource(currentItem.getPaymentImage());
        holder.paymentText.setText(currentItem.getPaymentText());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
