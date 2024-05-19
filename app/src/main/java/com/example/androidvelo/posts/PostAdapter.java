package com.example.androidvelo.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidvelo.databinding.ItemPostBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class PostAdapter extends FirebaseRecyclerAdapter<Post, PostAdapter.PostViewHolder> {

    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = ItemPostBinding.inflate(inflater, parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
        holder.bind(model);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public PostViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post post) {
            binding.textViewPostContent.setText(post.getContent());
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                binding.imageViewPost.setVisibility(View.VISIBLE);
                Glide.with(binding.getRoot()).load(post.getImageUrl()).into(binding.imageViewPost);
            } else {
                binding.imageViewPost.setVisibility(View.GONE);
            }
        }
    }
}
