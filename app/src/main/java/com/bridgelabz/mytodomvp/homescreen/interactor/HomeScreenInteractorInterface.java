package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.net.Uri;

/**
 * Created by bridgeit on 9/5/17.
 */
public interface HomeScreenInteractorInterface
{
    void getTodoNoteFromServer(String userId);
    void uploadProfilePic(String currentUserId, Uri selectedImage);
}
