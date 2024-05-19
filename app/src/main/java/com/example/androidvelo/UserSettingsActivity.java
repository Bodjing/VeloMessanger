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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserSettingsActivity extends AppCompatActivity {

    private EditText mDescriptionEditText;
    private Button mSaveButton;
    private DatabaseReference mDatabaseRef;
    private Uri mImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        mDescriptionEditText = findViewById(R.id.profile_description);
        mSaveButton = findViewById(R.id.save_profile_description);

        // Инициализируем ссылку на базу данных Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        // Обработчик нажатия на кнопку "Выбрать изображение"
        findViewById(R.id.profile_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Обработчик нажатия на кнопку "Сохранить"
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            // Установка выбранного изображения в ImageView
            ((ImageView) findViewById(R.id.profile_image_view)).setImageURI(mImageUri);
        }
    }

    private void saveProfile() {
        String description = mDescriptionEditText.getText().toString().trim();

        // Проверяем, что описание профиля не пустое и выбрано изображение
        if (description.isEmpty()) {
            Toast.makeText(this, "Введите описание профиля", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImageUri == null) {
            Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
            return;
        }

        // Сохраняем изображение и описание профиля в Firebase
        uploadImage(description);
    }

    private void uploadImage(final String description) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profile_images/" + userId);

        // Загружаем изображение в Firebase Storage
        profileImageRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Получаем URL загруженного изображения
                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                saveProfileToDatabase(description, imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserSettingsActivity.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveProfileToDatabase(String description, String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = mDatabaseRef.child(userId);

        // Сохраняем описание профиля и URL изображения в базу данных Firebase
        userRef.child("profileDescription").setValue(description);
        userRef.child("profileImage").setValue(imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserSettingsActivity.this, "Профиль сохранен успешно", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserSettingsActivity.this, "Ошибка при сохранении профиля", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
