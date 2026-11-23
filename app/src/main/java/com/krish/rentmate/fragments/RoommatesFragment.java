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
import com.krish.rentmate.repositories.AuthRepository;
import com.krish.rentmate.repositories.RepositoryCallback;
import com.krish.rentmate.repositories.RoommateRepository;

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

        allRoommates = new ArrayList<>();
        filteredRoommates = new ArrayList<>();

        TextInputEditText etSearch = view.findViewById(R.id.etSearchRoommates);
        tvCount = view.findViewById(R.id.tvCount);

        RecyclerView rvRoommates = view.findViewById(R.id.rvRoommates);
        rvRoommates.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoommateAdapter(filteredRoommates, this);
        rvRoommates.setAdapter(adapter);

        loadRoommates(etSearch);

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

        return view;
    }

    private void loadRoommates(TextInputEditText etSearch) {
        RoommateRepository.getInstance().getRoommates(new RepositoryCallback<List<Roommate>>() {
            @Override
            public void onSuccess(List<Roommate> result) {
                allRoommates = result != null ? result : new ArrayList<>();
                filter(etSearch.getText() != null ? etSearch.getText().toString() : "");
            }

            @Override
            public void onError(String errorMessage) {
                updateCount();
            }
        });
    }

    private void filter(String query) {
        filteredRoommates.clear();
        String q = query.toLowerCase().trim();

        for (Roommate r : allRoommates) {
            if (q.isEmpty() || (r.getName() != null && r.getName().toLowerCase().contains(q)) || 
                (r.getLocation() != null && r.getLocation().toLowerCase().contains(q))) {
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

        String currentUid = AuthRepository.getInstance().isUserAuthenticated() ? 
                AuthRepository.getInstance().getCurrentUser().getUid() : "guest";
        String currentName = AuthRepository.getInstance().isUserAuthenticated() && 
                AuthRepository.getInstance().getCurrentUser().getDisplayName() != null ? 
                AuthRepository.getInstance().getCurrentUser().getDisplayName() : "Krish";

        RoommateRepository.getInstance().sendConnectionRequest(currentUid, currentName, roommate.getUid(), new RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (getView() != null) {
                    Snackbar.make(getView(), "Connection request sent to " + roommate.getName() + "!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (getView() != null) {
                    Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}