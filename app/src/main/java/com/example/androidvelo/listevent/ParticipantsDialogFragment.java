package com.example.androidvelo.listevent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidvelo.R;

import java.util.List;

public class ParticipantsDialogFragment extends DialogFragment {

    private List<String> participantNames;

    public ParticipantsDialogFragment(List<String> participantNames) {
        this.participantNames = participantNames;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_participants, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ParticipantsAdapter adapter = new ParticipantsAdapter(participantNames);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
