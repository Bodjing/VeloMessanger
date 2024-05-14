package com.example.androidvelo.bottomnav.options;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidvelo.bottomnav.event.EventFragment;
import com.example.androidvelo.R;
import com.example.androidvelo.databinding.FragmentOptionsBinding;

public class OptionsFragment extends Fragment {
    private FragmentOptionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOptionsBinding.inflate(inflater, container, false);

        // Обработчик нажатия на кнопку event_btn
        binding.eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход к EventFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EventFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return binding.getRoot();
    }
}
