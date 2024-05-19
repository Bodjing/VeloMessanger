package com.example.androidvelo.bottomnav.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidvelo.databinding.FragmentHomeBinding;
import com.example.androidvelo.posts.CreatePostActivity;
import com.example.androidvelo.posts.Post;
import com.example.androidvelo.posts.PostAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseRecyclerAdapter<Post, PostAdapter.PostViewHolder> postAdapter;
    private DatabaseReference postsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Инициализируем базу данных Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        postsRef = database.getReference("posts");

        // Настройка RecyclerView
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsRef, Post.class)
                .build();
        postAdapter = new PostAdapter(options);
        binding.recyclerViewPosts.setAdapter(postAdapter);

        // Находим кнопку для создания поста
        binding.newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем активити для создания поста
                openCreatePostActivity();
            }
        });

        return rootView;
    }

    // Метод для открытия активити создания поста
    private void openCreatePostActivity() {
        Intent intent = new Intent(getActivity(), CreatePostActivity.class);
        startActivityForResult(intent, CREATE_POST_REQUEST);
    }

    // Константа для запроса создания поста
    private static final int CREATE_POST_REQUEST = 1;

    // Метод для обработки результата создания поста из активити
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_POST_REQUEST && resultCode == Activity.RESULT_OK) {
            // Обновляем список постов после создания нового поста
            postAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }
}
