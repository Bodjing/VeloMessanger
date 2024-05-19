package com.example.androidvelo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidvelo.event.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EventActivity extends AppCompatActivity {

    private EditText eventNameEditText, eventDescriptionEditText, eventDistanceEditText, eventAddressEditText;
    private Button createButton;
    private DatabaseReference databaseReference;
    private ImageView eventImageView;

    // Код запроса для выбора изображения
    private static final int PICK_IMAGE_REQUEST = 1;

    // Ссылка на хранилище Firebase
    private StorageReference storageReference;

    // Переменная для хранения URI выбранного изображения
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Инициализация базы данных Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("events");

        // Инициализация хранилища Firebase
        storageReference = FirebaseStorage.getInstance().getReference();

        // Инициализация элементов пользовательского интерфейса
        eventNameEditText = findViewById(R.id.event_name);
        eventDescriptionEditText = findViewById(R.id.event_description);
        eventDistanceEditText = findViewById(R.id.event_distance);
        eventAddressEditText = findViewById(R.id.event_address);
        createButton = findViewById(R.id.create_btn);
        eventImageView = findViewById(R.id.event_image_url);

        // Установка обработчика нажатия на eventImageView
        eventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открыть активность выбора изображения
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
            }
        });

        // Установка обработчика нажатия на кнопку createButton
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вызов метода для сохранения данных о мероприятии в базе данных Firebase
                saveEventToFirebase();
            }
        });
    }

    // Метод для обработки выбранного изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Получить URI выбранного изображения
            selectedImageUri = data.getData();

            // Здесь вы можете загрузить изображение в ваш ImageView или обработать его другим способом
            eventImageView.setImageURI(selectedImageUri);
        }
    }

    // Метод для сохранения данных о мероприятии в базе данных Firebase
    private void saveEventToFirebase() {
        // Получение значений из полей ввода
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();
        String eventDistance = eventDistanceEditText.getText().toString().trim();
        String eventAddress = eventAddressEditText.getText().toString().trim();

        // Генерация уникального eventId
        String eventId = databaseReference.push().getKey();

        // Проверка наличия выбранного изображения
        if (selectedImageUri != null) {
            // Создание ссылки для сохранения изображения в хранилище Firebase
            StorageReference filePath = storageReference.child("event_images").child(selectedImageUri.getLastPathSegment());

            // Загрузка изображения в хранилище Firebase
            filePath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Получение URL загруженного изображения
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // URL изображения успешно получен
                            String imageUrl = uri.toString();

                            // Создание объекта Event с imageUrl и eventId, и сохранение в базе данных Firebase
                            Event event = new Event(eventName, eventDescription, eventDistance, eventAddress, imageUrl);
                            event.setEventId(eventId); // Установка eventId

                            // Добавление данных о мероприятии в базу данных Firebase
                            databaseReference.child(eventId).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Данные успешно сохранены
                                        finish(); // Закрытие активности после успешного сохранения
                                    } else {
                                        // Ошибка при сохранении данных
                                        Toast.makeText(EventActivity.this, "Ошибка при сохранении мероприятия", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Ошибка при загрузке изображения
                    Toast.makeText(EventActivity.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Если изображение не выбрано, создаем объект Event без URL изображения и сохраняем в базе данных Firebase
            Event event = new Event(eventName, eventDescription, eventDistance, eventAddress, "");
            event.setEventId(eventId); // Установка eventId

            // Добавление данных о мероприятии в базу данных Firebase
            databaseReference.push().setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Данные успешно сохранены
                        finish(); // Закрытие активности после успешного сохранения
                    } else {
                        // Ошибка при сохранении данных
                        Toast.makeText(EventActivity.this, "Ошибка при сохранении мероприятия", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
