package com.karol.piekarski.mydiabetics_dzienniczek.Java.Fragments.Notes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.ApplicationStorage;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateNote extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText updateTitle;
    private EditText updateContent;
    private FirebaseFirestore firebaseFirestore;
    private ApplicationStorage applicationStorage;
    private FloatingActionButton saveUpdateNote;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    public UpdateNote() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateNote.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateNote newInstance(String param1, String param2) {
        UpdateNote fragment = new UpdateNote();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_update_note, container, false);

        updateTitle = view.findViewById(R.id.noteDetailsTitle);
        updateContent = view.findViewById(R.id.noteDetailsContent);
        firebaseFirestore = FirebaseFirestore.getInstance();
        applicationStorage = ApplicationStorage.getInstance();

        saveUpdateNote = view.findViewById(R.id.saveUpdateNote);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        updateTitle.setText(applicationStorage.getNoteViewHolder().getNoteTitle());
        updateContent.setText(applicationStorage.getNoteViewHolder().getNoteContent());

        saveUpdateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });

        return view;
    }

    private void updateNote()
    {
        String nContent = updateContent.getText().toString();
        String nTitle  = updateTitle.getText().toString();

        if(nContent.isEmpty() || nTitle.isEmpty())
        {
            Toast.makeText(getContext(), "Prosze wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        //progressBarAddNote.setVisibility(View.VISIBLE);
        closeKeyboard();

        DocumentReference documentReference = firebaseFirestore.collection("Notes").document(user.getUid()).collection("myNotes").document(applicationStorage.getNoteViewHolder().getNoteId());
        Map<String, Object> note= new HashMap<>();
        note.put("title", nTitle);
        note.put("content", nContent);
        Log.i("Test", applicationStorage.getNoteViewHolder().getNoteId());
        documentReference.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Notatka została zaktualizowana.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_updateNote_to_notes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}