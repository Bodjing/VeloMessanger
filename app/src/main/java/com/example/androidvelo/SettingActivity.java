package com.example.androidvelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private TextView settingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingUser = findViewById(R.id.setting_user);

        settingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь вы можете запустить новую активность или фрагмент для отображения настроек пользователя
                Intent intent = new Intent(SettingActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
        });

        // Находим кнопку back_profile_btn
        findViewById(R.id.back_profile_btn).setOnClickListener(new View.OnClickListener() {
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
