package com.example.a236333_hw3.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.a236333_hw3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SlideshowFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView first = root.findViewById(R.id.first_place);
        final TextView second = root.findViewById(R.id.second_place);
        final TextView third = root.findViewById(R.id.third_place);

        CollectionReference tasks =
                FirebaseFirestore.getInstance().collection("Users");

        tasks.orderBy("Grade", Query.Direction.DESCENDING).limit(3).get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 1;

                        for (final DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            final int curr = count;
                            SlideshowFragment.this.getActivity().runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (curr == 1) first.setText("1st: " +
                                                documentSnapshot.getString("Name") +
                                                ", " +
                                                documentSnapshot.getLong("Grade").toString());
                                        if (curr == 2) second.setText("2nd: " +
                                                documentSnapshot.getString("Name") +
                                                ", " +
                                                documentSnapshot.getLong("Grade").toString());
                                        if (curr == 3) third.setText("3rd: " +
                                                documentSnapshot.getString("Name") +
                                                ", " +
                                                documentSnapshot.getLong("Grade").toString());
                                    }
                                }
                            );

                            count ++;
                            if (count > 3) break;
                        }
                        if (count <= 1) first.setText("No results yet...");
                        if (count <= 2) second.setText("");
                        if (count <= 3) third.setText("");
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        first.setText("something went wrong, the connection with server is lost :(");
                        second.setText("");
                        third.setText("");
                    }
                }
        );

        return root;
    }
}
