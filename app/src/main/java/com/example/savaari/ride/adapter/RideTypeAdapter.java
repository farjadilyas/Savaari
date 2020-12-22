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

public class RideTypeAdapter  extends RecyclerView.Adapter<RideTypeAdapter.ItemViewHolder> {

    private ArrayList<RideTypeItem> mItemList;
    private ItemClickListener listener;

    public RideTypeAdapter(ArrayList<RideTypeItem> itemList, ItemClickListener listener) {
        mItemList = itemList;
        this.listener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeakReference<ItemClickListener> listenerRef;
        public LinearLayout rideTypeConfig;
        private ImageView rideTypeImage;
        private TextView rideTypeName, rideTypePrice;

        public ItemViewHolder(@NonNull View itemView, final ItemClickListener listener) {
            super(itemView);

            rideTypeConfig = itemView.findViewById(R.id.ride_type_config);
            rideTypeImage = itemView.findViewById(R.id.ride_type_img);
            rideTypeName = itemView.findViewById(R.id.ride_type_name);
            rideTypePrice = itemView.findViewById(R.id.ride_type_price);

            listenerRef = new WeakReference<>(listener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onRideTypeItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RideTypeAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_type_card, parent, false);
        return new ItemViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RideTypeAdapter.ItemViewHolder holder, int position) {

        RideTypeItem currentItem = mItemList.get(position);

        holder.rideTypeImage.setImageResource(currentItem.getRideTypeImage());
        holder.rideTypeName.setText(currentItem.getRideTypeName());
        holder.rideTypePrice.setText(currentItem.getRideTypePrice());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
