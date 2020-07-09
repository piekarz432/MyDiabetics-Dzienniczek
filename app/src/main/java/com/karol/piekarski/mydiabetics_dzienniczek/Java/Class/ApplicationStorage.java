package com.karol.piekarski.mydiabetics_dzienniczek.Java.Class;

import com.karol.piekarski.mydiabetics_dzienniczek.Java.Fragments.Notes.NotesFragment;

public class ApplicationStorage {

    private volatile static ApplicationStorage instance;
    private boolean isLoggedGoogle;
    private NotesFragment.NoteViewHolder noteViewHolder;


    public void setLoggedGoogle(boolean loggedGoogle) {
        isLoggedGoogle = loggedGoogle;
    }

    public boolean isLoggedGoogle() {
        return isLoggedGoogle;
    }

    public NotesFragment.NoteViewHolder getNoteViewHolder() {
        return noteViewHolder;
    }

    public void setNoteViewHolder(NotesFragment.NoteViewHolder noteViewHolder) {
        this.noteViewHolder = noteViewHolder;
    }

    private ApplicationStorage() {}

    public static ApplicationStorage getInstance() {
        if (instance == null) {
            synchronized (ApplicationStorage.class) {
                if (instance == null) {
                    instance = new ApplicationStorage();
                }
            }
        }

        return instance;
    }
}
