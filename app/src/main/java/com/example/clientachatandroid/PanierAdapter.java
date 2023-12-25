package com.example.clientachatandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clientachatandroid.R;
import com.example.clientachatandroid.model.Article;

import java.util.List;

public class PanierAdapter extends RecyclerView.Adapter<PanierAdapter.ArticleViewHolder> {
    private List<Article> articles;
    private OnItemClickListener listener;

    // Constructeur, méthodes pour ajouter/supprimer des articles, etc.

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    // Constructeur de l'adaptateur
    public PanierAdapter(List<Article> articles, OnItemClickListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView;
        TextView prixTextView;
        TextView quantityTextView;
        ImageButton deleteButton;

        ArticleViewHolder(View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.nomTextView);
            prixTextView = itemView.findViewById(R.id.prixTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.nomTextView.setText(article.getNom());
        holder.prixTextView.setText(String.valueOf(article.getPrix()));
        holder.quantityTextView.setText(String.valueOf(article.getQuantite()));

        holder.deleteButton.setOnClickListener(view -> {
            // Supprimez l'article à la position donnée
            articles.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
