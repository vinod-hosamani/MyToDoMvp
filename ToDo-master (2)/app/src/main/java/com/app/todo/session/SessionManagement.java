package com.app.todo.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.app.todo.constants.Constant;
import com.app.todo.registration.model.UserModel;
import com.app.todo.login.ui.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class SessionManagement {
    static SharedPreferences userDataPref;
    static Editor userEditor;
    static Context context;

    public SessionManagement(Context context){
        this.context = context;
        if(userDataPref == null && userEditor == null) {
            userDataPref = this.context.getSharedPreferences
                    (Constant.user_pref_name, MODE_PRIVATE);
            userEditor = userDataPref.edit();
        }
    }

    public void put(UserModel userModel, boolean fb_login, boolean isGoogleLogin){
        userEditor.putString(Constant.key_id, userModel.getId());
        userEditor.putString(Constant.key_fullname, userModel.getFullname());
        userEditor.putString(Constant.key_email, userModel.getEmail());
        userEditor.putString(Constant.key_password, userModel.getPassword());
        userEditor.putString(Constant.key_mobile, userModel.getMobile());
        if (userModel.getImageUrl() != null) {
            userEditor.putString(Constant.key_pro_pic, userModel.getImageUrl().toString());
        }
        userEditor.putBoolean(Constant.key_check_fb_login, fb_login);
        userEditor.putBoolean(Constant.key_check_google_login, isGoogleLogin);
        userEditor.putBoolean(Constant.key_is_login, true);
        userEditor.commit();
    }

    public void setProfile(Uri downloadUrl){
        userEditor.putString(Constant.key_pro_pic, downloadUrl.toString());
        userEditor.commit();
    }

    public UserModel getUserDetails(){
        UserModel user = new UserModel();
        user.setFullname(userDataPref.getString(Constant.key_fullname,Constant.empty_value));
        user.setEmail(userDataPref.getString(Constant.key_email,Constant.empty_value));
        user.setPassword(userDataPref.getString(Constant.key_password,Constant.empty_value));
        user.setMobile(userDataPref.getString(Constant.key_mobile,Constant.empty_value));
        user.setId(userDataPref.getString(Constant.key_id,Constant.empty_value));
        if (userDataPref.getString(Constant.key_pro_pic
                , Constant.empty_value).equals("")) {
            user.setImageUrl(userDataPref.getString(Constant.key_pro_pic, Constant.empty_value));
        }else {
            user.setImageUrl("");
        }
        return  user;
    }

    public boolean isLoggedIn(){
        return userDataPref.getBoolean(Constant.key_is_login,false);
    }

    public boolean isFbLoggedIn(){
        return userDataPref.getBoolean(Constant.key_check_fb_login,false);
    }

    public boolean isGoogleLoggedIn(){
        return userDataPref.getBoolean(Constant.key_check_google_login,false);
    }


    public void logoutUser(){
        userEditor.remove(Constant.key_id);
        userEditor.remove(Constant.key_email);
        userEditor.remove(Constant.key_fullname);
        userEditor.remove(Constant.key_mobile);
        userEditor.remove(Constant.key_password);

        userEditor.putBoolean(Constant.key_is_login, false);
        userEditor.putBoolean(Constant.key_check_fb_login, false);
        userEditor.putBoolean(Constant.key_check_google_login, false);

        userEditor.commit();

        LoginManager.getInstance().logOut();    /*fb logout*/

        FirebaseAuth.getInstance().signOut();   /*google signout*/

        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }
}