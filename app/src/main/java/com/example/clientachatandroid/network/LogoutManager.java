package com.example.clientachatandroid.network;

import android.os.AsyncTask;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class LogoutManager {
    private Model model;

    public LogoutManager() throws SQLException, IOException, ClassNotFoundException {
        this.model = Model.getInstance(null);
    }

    public void performLogoutAsync(OnLogoutCompleteListener listener) {
        new LogoutTask(listener).execute();
    }

    private class LogoutTask extends AsyncTask<Void, Void, Void> {
        private final OnLogoutCompleteListener listener;

        public LogoutTask(OnLogoutCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                model.on_pushLogout();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // À la fin, appeler la méthode de l'interface lorsque l'opération est terminée
            if (listener != null) {
                listener.onLogoutSuccess();
            }

            return null;
        }
    }

    // Interface pour écouter la fin du login
    public interface OnLogoutCompleteListener {
        void onLogoutSuccess();
    }
}
