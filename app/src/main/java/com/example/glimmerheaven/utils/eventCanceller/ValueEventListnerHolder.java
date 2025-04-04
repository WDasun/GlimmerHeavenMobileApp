package com.example.glimmerheaven.utils.eventCanceller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ValueEventListnerHolder {
    private DatabaseReference reference;
    private ValueEventListener eventListener;

    public ValueEventListnerHolder(DatabaseReference reference, ValueEventListener eventListener) {
        this.reference = reference;
        this.eventListener = eventListener;
    }
    public void removeListener(){
     reference.removeEventListener(eventListener);
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public ValueEventListener getEventListener() {
        return eventListener;
    }
}
