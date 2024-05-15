package com.example.androidvelo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidvelo.event.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventActivity extends AppCompatActivity {

    private EditText eventName;
    private EditText eventDescription;
    private EditText eventDistance;
    private EditText eventAddress;
    private EditText eventImageUrl; // Новое поле для URL изображения
    private Button createBtn;
    private DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventName = findViewById(R.id.event_name);
        eventDescription = findViewById(R.id.event_description);
        eventDistance = findViewById(R.id.event_distance);
        eventAddress = findViewById(R.id.event_address);
        eventImageUrl = findViewById(R.id.event_image_url); // Инициализация нового поля
        createBtn = findViewById(R.id.create_btn);

        // Инициализация Firebase Database
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }

    private void saveEvent() {
        String name = eventName.getText().toString().trim();
        String description = eventDescription.getText().toString().trim();
        String distance = eventDistance.getText().toString().trim();
        String address = eventAddress.getText().toString().trim();
        String imageUrl = eventImageUrl.getText().toString().trim(); // Получение URL изображения

        if (name.isEmpty() || description.isEmpty() || distance.isEmpty() || address.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Генерация уникального ID мероприятия
        String eventId = databaseEvents.push().getKey();

        // Создание объекта мероприятия
        Event event = new Event(name, eventId, description, distance, address, imageUrl);

        // Сохранение объекта мероприятия в Firebase
        if (eventId != null) {
            databaseEvents.child(eventId).setValue(event);
            Toast.makeText(this, "Event created", Toast.LENGTH_SHORT).show();
            clearFields();
            finish(); // Закрытие активности после создания события
        } else {
            Toast.makeText(this, "Error creating event", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        eventName.setText("");
        eventDescription.setText("");
        eventDistance.setText("");
        eventAddress.setText("");
        eventImageUrl.setText(""); // Очистка поля URL изображения
    }
}
