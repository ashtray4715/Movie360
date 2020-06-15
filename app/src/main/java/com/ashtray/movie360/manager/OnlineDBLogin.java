package com.ashtray.movie360.manager;

import android.app.Activity;

import com.ashtray.movie360.MyApplication;
import com.ashtray.movie360.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OnlineDBLogin {

    public interface LoginCallBack {
        void onLoginSuccess(String adminName);
        void onLoginFailed(String errorMessage);
    }

    private LoginCallBack loginCallBack;

    public OnlineDBLogin(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    public void tryToSignIn(Activity activity, String userName, String password) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            tryToMatchUserNamePassword(userName, password);
            return;
        }

        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(activity, task -> {
            if (!task.isSuccessful()) {
                if(loginCallBack != null)
                    loginCallBack.onLoginFailed("Not complete");
                return;
            }

            tryToMatchUserNamePassword(userName, password);
        });
    }

    private void tryToMatchUserNamePassword(String username, String password) {
        String collectionName = MyApplication.getInstance().getString(R.string.admin_login_collection);
        String documentName = MyApplication.getInstance().getString(R.string.admin_login_document);
        String fieldName = MyApplication.getInstance().getString(R.string.admin_login_field);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firebaseFirestore.collection(collectionName).document(documentName);
        docRef.get().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                if(loginCallBack != null)
                    loginCallBack.onLoginFailed("No db access");
                return;
            }

            try {
                DocumentSnapshot document = task.getResult();
                if(document == null || document.getData() == null || document.getData().get(fieldName) == null){
                    if(loginCallBack != null)
                        loginCallBack.onLoginFailed("Document error");
                    return;
                }

                Object credentialData = document.getData().get(fieldName);
                List<String> credentialList = objectToStringList(credentialData);
                for (String current: credentialList) {

                    String[] infos = current.split("#");
                    if(infos.length == 3){
                        boolean userNameMatched = infos[0].equals(username);
                        boolean passwordMatched = infos[1].equals(password);
                        if(userNameMatched && passwordMatched) {
                            if(loginCallBack != null)
                                loginCallBack.onLoginSuccess(infos[2]);
                            return;
                        }
                    }

                }

                if(loginCallBack != null)
                    loginCallBack.onLoginFailed("Wrong username password");

            } catch (Exception e) {
                e.printStackTrace();
                if(loginCallBack != null)
                    loginCallBack.onLoginFailed("Data processing error");
            }

        }).addOnCanceledListener(() -> {
            if(loginCallBack != null)
                loginCallBack.onLoginFailed("Wrong ref.");
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends List<?>> T objectToStringList(Object obj) {
        return (T) obj;
    }

}
