package com.karol.piekarski.mydiabetics_dzienniczek.Java.Fragments.Notes;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.ApplicationStorage;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Class.EmptyRecyclerView;
import com.karol.piekarski.mydiabetics_dzienniczek.Java.Model.Notes;
import com.karol.piekarski.mydiabetics_dzienniczek.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EmptyRecyclerView listNotes;
    private View emptyRecyclerView;
    private FloatingActionButton floatingActionButtonNotes;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter<Notes, NoteViewHolder> noteAdapter;
    private FirestoreRecyclerOptions<Notes> allNotes;
    private ApplicationStorage applicationStorage;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;




    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        applicationStorage = ApplicationStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        listNotes = view.findViewById(R.id.listNotes);
        emptyRecyclerView = view.findViewById(R.id.emptyViewNotes);
        floatingActionButtonNotes = view.findViewById(R.id.floatingActionButtonNotes);


        floatingActionButtonNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_notes_to_addNote);
            }
        });

        createItem();
        deleteItem();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(noteAdapter !=null)
        {
            noteAdapter.stopListening();
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTitle;
        private TextView noteContent;
        private CardView noteCard;
        private String noteId;
        private View view;

        public String getNoteId() {
            return noteId;
        }

        public void setNoteId(String noteId) {
            this.noteId = noteId;
        }

        public View getView() {
            return view;
        }

        public String getNoteTitle() {
            return noteTitle.getText().toString();
        }

        public String getNoteContent() {
            return noteContent.getText().toString();
        }

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            noteCard = itemView.findViewById(R.id.noteCard);
            view=itemView;
        }


    }

    private void createItem() {
        Query query = firebaseFirestore.collection("Notes").document(user.getUid()).collection("myNotes").orderBy("title");

        allNotes = new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query, Notes.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Notes, NoteViewHolder>(allNotes) {
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
                return new NoteViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Notes notes) {
                noteViewHolder.noteTitle.setText(notes.getTitle());
                noteViewHolder.noteContent.setText(notes.getContent());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    noteViewHolder.noteCard.setCardBackgroundColor((noteViewHolder.view.getResources().getColor(getRandomColor(), null)));
                }

                String noteId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                noteViewHolder.noteId = noteId;

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        applicationStorage.setNoteViewHolder(noteViewHolder);
                        Navigation.findNavController(view).navigate(R.id.action_notes_to_noteDetails);
                    }
                });
            }

        };
        listNotes.setEmptyView(emptyRecyclerView);
        listNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        //listNotes.setLayoutManager((new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        listNotes.setAdapter(noteAdapter);
    }

    private void deleteItem() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteAdapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getReference().delete();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addActionIcon(R.drawable.icon_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(listNotes);
    }

    private int getRandomColor() {
        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.blue);
        colors.add(R.color.yellow);
        colors.add(R.color.skyblue);
        colors.add(R.color.lightPurple);
        colors.add(R.color.lightGreen);
        colors.add(R.color.gray);
        colors.add(R.color.pink);
        colors.add(R.color.red);
        colors.add(R.color.greenlight);
        colors.add(R.color.notgreen);

        Random r = new Random();
        int number = r.nextInt(colors.size());
        return colors.get(number);
    }


}