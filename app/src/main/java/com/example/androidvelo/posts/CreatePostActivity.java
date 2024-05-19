package com.example.androidvelo.posts;

import android.app.ProgressDialog;
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

import com.bumptech.glide.Glide;
import com.example.androidvelo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePostActivity extends AppCompatActivity {
    private EditText editTextPostContent;
    private Button buttonPublishPost;
    private ImageView imageViewPreview;
    private DatabaseReference postsRef;
    private StorageReference imageStorageRef;
    private ProgressDialog progressDialog;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Инициализируем базу данных Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        postsRef = database.getReference("posts");

        // Инициализируем хранилище Firebase для изображений
        FirebaseStorage storage = FirebaseStorage.getInstance();
        imageStorageRef = storage.getReference().child("post_images");

        // Инициализируем элементы пользовательского интерфейса
        editTextPostContent = findViewById(R.id.edit_text_post_content);
        buttonPublishPost = findViewById(R.id.button_publish_post);
        imageViewPreview = findViewById(R.id.image_view_preview);

        // Обработчик нажатия на изображение для выбора изображения
        imageViewPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем галерею для выбора изображения
                openGallery();
            }
        });

        // Обработчик нажатия на кнопку "Опубликовать"
        buttonPublishPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверяем, выбрано ли изображение
                if (selectedImageUri != null) {
                    // Если изображение выбрано, загружаем его
                    uploadImage();
                } else {
                    // Если изображение не выбрано, публикуем пост без изображения
                    publishPost(null);
                }
            }
        });

        // Инициализируем прогресс-диалог
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Загрузка изображения...");
    }

    // Метод для открытия галереи для выбора изображения
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // Метод для обработки результата выбора изображения из галереи
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Показываем выбранное изображение в предварительном просмотре
            Glide.with(this).load(selectedImageUri).into(imageViewPreview);
        }
    }

    // Метод для загрузки изображения в Firebase Storage
    private void uploadImage() {
        progressDialog.show();
        StorageReference imageRef = imageStorageRef.child(System.currentTimeMillis() + ".jpg");

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Получаем URL загруженного изображения
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Загружаем текст поста и URL изображения в базу данных
                                publishPost(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Обработка ошибок при загрузке изображения
                        progressDialog.dismiss();
                        Toast.makeText(CreatePostActivity.this, "Ошибка загрузки изображения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Метод для публикации поста
    private void publishPost(String imageUrl) {
        String postContent = editTextPostContent.getText().toString().trim();

        // Проверка на пустое поле ввода
        if (!postContent.isEmpty()) {
            // Генерируем уникальный ключ для нового поста
            String postId = postsRef.push().getKey();

            // Создаем объект Post для сохранения в базе данных
            Post post = new Post(postId, postContent, imageUrl);

            // Сохраняем пост в базе данных
            postsRef.child(postId).setValue(post);

            // Выводим сообщение об успешной публикации
            Toast.makeText(this, "Пост успешно опубликован", Toast.LENGTH_SHORT).show();

            // Закрываем активити
            finish();
        } else {
            // Если поле ввода пустое, выводим сообщение об ошибке
            Toast.makeText(this, "Введите текст поста", Toast.LENGTH_SHORT).show();
        }
    }
}
