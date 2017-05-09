package com.bridgelabz.mytodomvp.homescreen.model;

/**
 * Created by bridgeit on 9/5/17.
 */
public class TodoItemModel  {
    private int noteId;
    private String title;
    private String note;
    private String reminderDate;
    private String startDate;
    private String isArchived;

    public String getIsArchived() {
        return isArchived;
    }

    public String getNote() {
        return note;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setIsArchived(String isArchived) {
        this.isArchived = isArchived;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
