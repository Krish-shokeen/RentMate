package com.krish.rentmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.krish.rentmate.R;
import com.krish.rentmate.models.Roommate;

import java.util.List;
import java.util.Locale;

public class RoommateAdapter extends RecyclerView.Adapter<RoommateAdapter.RoommateViewHolder> {

    public interface OnRoommateClickListener {
        void onViewProfileClick(Roommate roommate);
        void onConnectClick(Roommate roommate, int position);
    }

    private List<Roommate> roommateList;
    private final OnRoommateClickListener listener;

    public RoommateAdapter(List<Roommate> roommateList, OnRoommateClickListener listener) {
        this.roommateList = roommateList;
        this.listener = listener;
    }

    public void updateList(List<Roommate> newList) {
        this.roommateList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoommateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roommate, parent, false);
        return new RoommateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoommateViewHolder holder, int position) {
        Roommate roommate = roommateList.get(position);
        holder.bind(roommate, listener);
    }

    @Override
    public int getItemCount() {
        return roommateList != null ? roommateList.size() : 0;
    }

    static class RoommateViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvNameAge, tvProfession, tvLocationBudget, tvCompatibility;
        ChipGroup chipGroupTags;
        MaterialButton btnViewProfile, btnConnect;

        public RoommateViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvNameAge = itemView.findViewById(R.id.tvNameAge);
            tvProfession = itemView.findViewById(R.id.tvProfession);
            tvLocationBudget = itemView.findViewById(R.id.tvLocationBudget);
            tvCompatibility = itemView.findViewById(R.id.tvCompatibility);
            chipGroupTags = itemView.findViewById(R.id.chipGroupTags);
            btnViewProfile = itemView.findViewById(R.id.btnViewProfile);
            btnConnect = itemView.findViewById(R.id.btnConnect);
        }

        public void bind(Roommate roommate, OnRoommateClickListener listener) {
            ivAvatar.setImageResource(roommate.getAvatarResId());
            tvNameAge.setText(String.format(Locale.US, "%s, %d", roommate.getName(), roommate.getAge()));
            tvProfession.setText(roommate.getProfession());
            tvLocationBudget.setText(String.format(Locale.US, "%s • ₹%dk–₹%dk", 
                    roommate.getLocation(), roommate.getMinBudget() / 1000, roommate.getMaxBudget() / 1000));
            tvCompatibility.setText(String.format(Locale.US, "%d%% Match", roommate.getCompatibility()));

            chipGroupTags.removeAllViews();
            if (roommate.getTags() != null) {
                for (String tag : roommate.getTags()) {
                    Chip chip = new Chip(itemView.getContext());
                    chip.setText(tag);
                    chip.setChipBackgroundColorResource(R.color.light_gray);
                    chip.setTextColor(itemView.getContext().getColor(R.color.dark_navy));
                    chip.setClickable(false);
                    chipGroupTags.addView(chip);
                }
            }

            if (roommate.isConnected()) {
                btnConnect.setText("Request Sent");
                btnConnect.setEnabled(false);
            } else {
                btnConnect.setText("Connect");
                btnConnect.setEnabled(true);
            }

            btnViewProfile.setOnClickListener(v -> listener.onViewProfileClick(roommate));
            btnConnect.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onConnectClick(roommate, pos);
                }
            });
        }
    }
}