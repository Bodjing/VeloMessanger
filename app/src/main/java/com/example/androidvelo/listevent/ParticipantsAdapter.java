package com.example.androidvelo.listevent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidvelo.R;

import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

    private List<String> participantNames;

    public ParticipantsAdapter(List<String> participantNames) {
        this.participantNames = participantNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String participantName = participantNames.get(position);
        holder.bind(participantName);
    }

    @Override
    public int getItemCount() {
        return participantNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView participantNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            participantNameTextView = itemView.findViewById(R.id.participant_name_text_view);
        }

        public void bind(String participantName) {
            participantNameTextView.setText(participantName);
        }
    }
}
