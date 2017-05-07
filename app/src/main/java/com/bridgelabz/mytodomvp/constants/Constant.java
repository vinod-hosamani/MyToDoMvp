package com.bridgelabz.mytodomvp.constants;

/**
 * Created by bridgeit on 7/5/17.
 */
public class Constant {
    public static final int splashTimeOut = 500;
    public static final String key_is_login = "IsLoggedIn";
    public static final String key_mobile = "mobile";
    public static final String key_fullname = "fullname";
    public static final String key_password = "password";
    public static final String key_email = "email";
    public static final String key_id = "id";
    public static final String user_pref_name = "userData";
    public static final String empty_value = "";

    public static final String key_title = "title";
    public static final String key_note = "note";

    public static final String key_firebase_user = "users";

    public static String mobilepattern="^(\\+91-|\\+91|0)?[7-9]{1}([0-9]){9}$";
    /*public static String email_pattern="^([a-z][A-Z][0-9]\\.){3,}@([a-z]){3,}\\.[a-z]+$";*/
    public static String email_pattern="^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()" +
            "\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}" +
            "\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public static final String key_firebase_todo = "todoData";
    public static final String key_fb_user = "email";
    public static final String key_fb_name = "name";
    public static final String key_fb_id = "id";
    public static final String key_check_fb_login = "isFbLogin";
    public static final int google_sign_in_req_code = 999;
    public static final String key_check_google_login = "isGoogleLogin" ;
    public static final String key_reminder = "key_reminder";
    public static final String key_note_id = "noteId";
    public static final String key_startDate = "startDate";
    public static final String key_firebase_archive = "archievedData";
}
