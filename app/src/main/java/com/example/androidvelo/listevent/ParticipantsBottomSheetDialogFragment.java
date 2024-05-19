package com.example.androidvelo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ParticipantsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private List<String> participantNames;

    public ParticipantsBottomSheetDialogFragment(List<String> participantNames) {
        this.participantNames = participantNames;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants_bottom_sheet_dialog, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ParticipantsAdapter(participantNames));

        return view;
    }

    private static class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder> {
        private List<String> participantNames;

        ParticipantsAdapter(List<String> participantNames) {
            this.participantNames = participantNames;
        }

        @NonNull
        @Override
        public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
            return new ParticipantViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
            holder.bind(participantNames.get(position));
        }

        @Override
        public int getItemCount() {
            return participantNames.size();
        }

        static class ParticipantViewHolder extends RecyclerView.ViewHolder {
            private TextView participantNameTextView;

            ParticipantViewHolder(@NonNull View itemView) {
                super(itemView);
                participantNameTextView = itemView.findViewById(R.id.participant_name_text_view);
            }

            void bind(String participantName) {
                participantNameTextView.setText(participantName);
            }
        }
    }
}
