package com.example.androidvelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidvelo.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Находим кнопку back_profile_btn
        binding.backProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для перехода на MainActivity
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                // Добавляем флаги, чтобы очистить стек задач и установить новый фрагмент
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Передаем в MainActivity индекс вкладки, соответствующей профилю
                intent.putExtra("TAB_INDEX", 1);
                startActivity(intent);
                // Закрываем текущую активность
                finish();
            }
        });
    }
}
