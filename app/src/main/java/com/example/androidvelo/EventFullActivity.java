package com.example.androidvelo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.androidvelo.event.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFullActivity extends AppCompatActivity {

    private DatabaseReference databaseEvents;
    private TextView eventNameTextView;
    private TextView eventDescriptionTextView;
    private TextView eventDistanceTextView;
    private TextView eventAddressTextView;
    private ImageView eventImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_full);

        // Получаем eventId из экстра
        String eventId = getIntent().getStringExtra("eventId");

        if (eventId != null) {
            // Инициализация Firebase Database
            databaseEvents = FirebaseDatabase.getInstance().getReference("events").child(eventId);

            // Инициализация Views
            eventNameTextView = findViewById(R.id.event_name_header_text_view);
            eventDescriptionTextView = findViewById(R.id.event_description_text_view);
            eventDistanceTextView = findViewById(R.id.event_distance_text_view);
            eventAddressTextView = findViewById(R.id.event_address_text_view);
            eventImageView = findViewById(R.id.event_image_view);

            // Загрузка данных о мероприятии из Firebase
            loadEventData();

            // Настройка кнопок
            Button acceptButton = findViewById(R.id.accept_btn);
            Button listParticipantsButton = findViewById(R.id.list_paticipants_btn);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerForEvent();
                }
            });

            listParticipantsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showParticipantsList();
                }
            });
        } else {
            // Обработка ситуации, когда eventId равен null
            Log.e("EventFullActivity", "eventId is null");
        }
    }

    private void loadEventData() {
        databaseEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                if (event != null) {
                    // Отображаем данные о мероприятии
                    eventNameTextView.setText(event.getNameEvent());
                    eventDescriptionTextView.setText(event.getDescription());
                    eventDistanceTextView.setText(getString(R.string.distance_format, event.getDistance()));
                    eventAddressTextView.setText(event.getAddress());

                    // Загрузка изображения с помощью Glide
                    Glide.with(EventFullActivity.this)
                            .load(event.getImageUrl())
                            .placeholder(R.drawable.baseline_assignment_event)
                            .error(R.drawable.baseline_assignment_event)
                            .into(eventImageView);
                } else {
                    Log.e("EventFullActivity", "Event is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении из базы данных
                Log.e("EventFullActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void registerForEvent() {
        // Получаем идентификатор текущего пользователя
        String currentUserId = getCurrentUserId();

        if (currentUserId != null) {
            // Добавляем пользователя в список участников мероприятия в базе данных
            databaseEvents.child("participants").child(currentUserId).setValue(true)
                    .addOnSuccessListener(aVoid -> Toast.makeText(EventFullActivity.this, "Вы успешно зарегистрировались на мероприятие", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Log.e("EventFullActivity", "Failed to register for the event: " + e.getMessage());
                        Toast.makeText(EventFullActivity.this, "Failed to register for the event", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("EventFullActivity", "Current user id is null");
        }
    }

    private void showParticipantsList() {
        databaseEvents.child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> participantIds = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String participantId = snapshot.getKey();
                    participantIds.add(participantId);
                }
                if (!participantIds.isEmpty()) {
                    getUsersNames(participantIds);
                } else {
                    Toast.makeText(EventFullActivity.this, "Нет участников принявших участие", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EventFullActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void getUsersNames(List<String> participantIds) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        List<String> participantNames = new ArrayList<>();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                if (username != null) {
                    participantNames.add(username);
                    // Если получили имена всех участников, отображаем их
                    if (participantNames.size() == participantIds.size()) {
                        showParticipantsAlertDialog(participantNames);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EventFullActivity", "Database error: " + databaseError.getMessage());
            }
        };

        for (String participantId : participantIds) {
            usersRef.child(participantId).addListenerForSingleValueEvent(listener);
        }
    }

    private void showParticipantsAlertDialog(List<String> participantNames) {
        StringBuilder message = new StringBuilder();
        for (String participantName : participantNames) {
            message.append(participantName).append("\n");
        }

        // Создаем пользовательский диалог для отображения списка участников
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Participants");
        builder.setMessage(message.toString());
        builder.setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private String getCurrentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            Log.e("EventFullActivity", "Current user is null");
            return null;
        }
    }
}
