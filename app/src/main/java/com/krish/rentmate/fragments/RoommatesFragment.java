package com.krish.rentmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.krish.rentmate.R;
import com.krish.rentmate.activities.RoommateDetailsActivity;
import com.krish.rentmate.adapters.RoommateAdapter;
import com.krish.rentmate.models.Roommate;
import com.krish.rentmate.utils.DemoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoommatesFragment extends Fragment implements RoommateAdapter.OnRoommateClickListener {

    private RoommateAdapter adapter;
    private List<Roommate> allRoommates;
    private List<Roommate> filteredRoommates;
    private TextView tvCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roommates, container, false);

        allRoommates = DemoData.getRoommates();
        filteredRoommates = new ArrayList<>(allRoommates);

        TextInputEditText etSearch = view.findViewById(R.id.etSearchRoommates);
        tvCount = view.findViewById(R.id.tvCount);

        RecyclerView rvRoommates = view.findViewById(R.id.rvRoommates);
        rvRoommates.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoommateAdapter(filteredRoommates, this);
        rvRoommates.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateCount();
        return view;
    }

    private void filter(String query) {
        filteredRoommates.clear();
        String q = query.toLowerCase().trim();

        for (Roommate r : allRoommates) {
            if (q.isEmpty() || r.getName().toLowerCase().contains(q) || r.getLocation().toLowerCase().contains(q)) {
                filteredRoommates.add(r);
            }
        }
        adapter.updateList(filteredRoommates);
        updateCount();
    }

    private void updateCount() {
        if (tvCount != null) {
            tvCount.setText(String.format(Locale.US, "%d Roommates matching", filteredRoommates.size()));
        }
    }

    @Override
    public void onViewProfileClick(Roommate roommate) {
        Intent intent = new Intent(getActivity(), RoommateDetailsActivity.class);
        intent.putExtra("roommate", roommate);
        startActivity(intent);
    }

    @Override
    public void onConnectClick(Roommate roommate, int position) {
        roommate.setConnected(true);
        adapter.notifyItemChanged(position);
        if (getView() != null) {
            Snackbar.make(getView(), "Connection request sent to " + roommate.getName() + "!", Snackbar.LENGTH_SHORT).show();
        }
    }
}