package com.example.androidvelo.bottomnav.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidvelo.EventActivity;
import com.example.androidvelo.EventFullActivity;
import com.example.androidvelo.R;
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
    private DatabaseReference databaseEvents;
    private List<Event> eventList;
    private EventAdapter eventAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        // Инициализация Firebase Database
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        // Инициализация списка и адаптера
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext(), eventList);

        // Настройка ListView
        ListView eventListView = rootView.findViewById(R.id.event_list_view);
        eventListView.setAdapter((ListAdapter) eventAdapter);

        // Загрузка данных из Firebase
        loadEvents();

        // В обработчике нажатия на элемент списка
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Открываем новую Activity с подробной информацией о мероприятии
                Intent intent = new Intent(getActivity(), EventFullActivity.class);
                intent.putExtra("eventId", eventList.get(position).getEventId());
                startActivity(intent);
            }
        });


        // Устанавливаем обработчик нажатия на кнопку создания мероприятия
        rootView.findViewById(R.id.create_event_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для запуска EventActivity
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
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
