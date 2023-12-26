package com.example.clientachatandroid.network;

import android.os.AsyncTask;
import com.example.clientachatandroid.databinding.PanierAdapter;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UpdateCartAsyncTask extends AsyncTask<Void, Void, List<Article>> {
    private final Model model;
    private final PanierAdapter adapter;

    public UpdateCartAsyncTask(PanierAdapter adapter) {
        try {
            this.model = Model.getInstance(null);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        this.adapter = adapter;
    }

    @Override
    protected List<Article> doInBackground(Void... voids) {

        try {
            model.getCaddie();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Article> updatedPanier) {
        adapter.notifyDataSetChanged();
    }
}
