package com.krish.rentmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krish.rentmate.R;
import com.krish.rentmate.models.Property;

import java.util.List;
import java.util.Locale;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    public interface OnPropertyClickListener {
        void onPropertyClick(Property property);
        void onFavoriteClick(Property property, int position);
    }

    private List<Property> propertyList;
    private final OnPropertyClickListener listener;

    public PropertyAdapter(List<Property> propertyList, OnPropertyClickListener listener) {
        this.propertyList = propertyList;
        this.listener = listener;
    }

    public void updateList(List<Property> newList) {
        this.propertyList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.bind(property, listener);
    }

    @Override
    public int getItemCount() {
        return propertyList != null ? propertyList.size() : 0;
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPropertyImage, ivFavorite;
        TextView tvPropertyType, tvPropertyTitle, tvRating, tvPropertyLocation, tvRent, tvBeds;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPropertyImage = itemView.findViewById(R.id.ivPropertyImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvPropertyType = itemView.findViewById(R.id.tvPropertyType);
            tvPropertyTitle = itemView.findViewById(R.id.tvPropertyTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPropertyLocation = itemView.findViewById(R.id.tvPropertyLocation);
            tvRent = itemView.findViewById(R.id.tvRent);
            tvBeds = itemView.findViewById(R.id.tvBeds);
        }

        public void bind(Property property, OnPropertyClickListener listener) {
            ivPropertyImage.setImageResource(property.getImageResId());
            tvPropertyType.setText(property.getType());
            tvPropertyTitle.setText(property.getTitle());
            tvRating.setText(String.valueOf(property.getRating()));
            tvPropertyLocation.setText(property.getLocation());
            tvRent.setText(String.format(Locale.US, "₹%,d / mo", property.getRent()));
            tvBeds.setText(String.format(Locale.US, "%d Bed%s", property.getBedrooms(), property.getBedrooms() > 1 ? "s" : ""));

            ivFavorite.setImageResource(property.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

            itemView.setOnClickListener(v -> listener.onPropertyClick(property));
            ivFavorite.setOnClickListener(v -> {
                property.setFavorite(!property.isFavorite());
                ivFavorite.setImageResource(property.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onFavoriteClick(property, pos);
                }
            });
        }
    }
}