package com.pm.formfitbackend.workout.dto;

public class UpdateExerciseNotesRequest {

    private String notes;

    public UpdateExerciseNotesRequest() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}