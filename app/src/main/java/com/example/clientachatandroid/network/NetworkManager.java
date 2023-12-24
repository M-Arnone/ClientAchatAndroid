package com.example.clientachatandroid.network;

import android.content.Context;
import android.os.AsyncTask;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class NetworkManager {

    private Context applicationContext;


    public NetworkManager(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void performLoginAsync(String username, String password, OnLoginCompleteListener listener) {
        new LoginTask(listener).execute(username, password);
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {
        private final OnLoginCompleteListener listener;

        public LoginTask(OnLoginCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Model m = Model.getInstance(applicationContext);
                m.on_pushLogin(params[0], params[1], false);
                //Boolean.parseBoolean(params[2])
            } catch (SQLException | IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // À la fin, appeler la méthode de l'interface lorsque l'opération est terminée
            if (listener != null) {
                listener.onLoginComplete();
            }

            return null;
        }
    }

    // Interface pour écouter la fin du login
    public interface OnLoginCompleteListener {
        void onLoginComplete();
    }
}
