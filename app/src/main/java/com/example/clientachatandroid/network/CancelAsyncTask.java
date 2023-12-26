package com.example.clientachatandroid.network;

import android.os.AsyncTask;
import com.example.clientachatandroid.databinding.PanierAdapter;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class CancelAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final Model model;
    private final PanierAdapter adapter;
    private final int id;

    public CancelAsyncTask(PanierAdapter adapter, int id) {
        this.id = id;
        try {
            this.model = Model.getInstance(null);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        this.adapter = adapter;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            if(id==-1)
                return model.on_pushViderPanier();
            else
                return model.on_pushSupprimerArticle(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Boolean cancelSuccessful) {
        if (cancelSuccessful)
            adapter.notifyDataSetChanged();
    }
}
