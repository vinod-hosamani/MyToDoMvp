package com.bridgelabz.mytodomvp.homescreen.model;

/**
 * Created by bridgeit on 9/5/17.
 */
public class TodoItemModel
{
    private int noteId;
    private String title;
    private String note;
    private String reminderDate;
    private String startDate;
    private boolean isArchived;
    private boolean isDeleted;
    private boolean isReminder;

    public TodoItemModel(boolean isArchived,boolean isDeleted,boolean isReminder, String note, int noteId, String reminderDate, String startDate, String title) {
        this.isArchived = isArchived;
        this.isDeleted=isDeleted;
        this.isReminder=isReminder;
        this.note = note;
        this.noteId = noteId;
        this.reminderDate = reminderDate;
        this.startDate = startDate;
        this.title = title;
    }

    public TodoItemModel()
    {

    }

    public boolean getIsArchived()
    {
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


    public  boolean isDeleted()
    {
        return  isDeleted;
    }

    public void setDeleted(boolean deleted)
    {
        isDeleted = deleted;
    }

    public boolean isArchieved()
    {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived)
    {
        this.isArchived = isArchived;
    }

    public boolean isReminder()
    {
        return isReminder;
    }

    public void setReminder(boolean reminder)
    {
        isReminder = reminder;
    }
}
