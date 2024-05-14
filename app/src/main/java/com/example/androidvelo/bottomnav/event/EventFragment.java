package com.example.androidvelo.bottomnav.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidvelo.databinding.FragmentEventBinding;
import com.example.androidvelo.databinding.FragmentOptionsBinding;

public class EventFragment extends Fragment {
    FragmentEventBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }
}
