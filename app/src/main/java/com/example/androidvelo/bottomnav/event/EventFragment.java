package com.example.androidvelo.bottomnav.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidvelo.EventActivity;
import com.example.androidvelo.databinding.FragmentEventBinding;
import com.example.androidvelo.event.Event;
import com.example.androidvelo.event.EventAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {
    FragmentEventBinding binding;
    private DatabaseReference databaseEvents;
    private List<Event> eventList;
    private EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);

        // Инициализация Firebase Database
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        // Инициализация списка и адаптера
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext(), eventList);

        // Настройка RecyclerView
        binding.eventRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.eventRv.setAdapter(eventAdapter);

        // Загрузка данных из Firebase
        loadEvents();

        // Устанавливаем обработчик нажатия на кнопку create_event_button
        binding.createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для запуска EventActivity
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void loadEvents() {
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении из базы данных
            }
        });
    }
}
