package com.example.collegeschedulerappfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.collegeschedulerappfinal.databinding.FragmentThirdBinding;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import android.content.SharedPreferences;
import android.content.Context;



public class ThirdFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String ASSIGNMENT_LIST_KEY = "assignmentList";
    private FragmentThirdBinding binding;

    private ArrayList<AssignmentModel> assignmentsList;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assignmentsList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.assignmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_assignment, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                AssignmentModel assignmentModel = assignmentsList.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;

                viewHolder.titleText.setText("Title: " + assignmentModel.getTitle());
                viewHolder.dueDateText.setText("Due Date: " + assignmentModel.getDueDate());
                viewHolder.classText.setText("Associated Class: " + assignmentModel.getAssociatedClass());

                viewHolder.editButton.setOnClickListener(v -> showEditAssignmentDialog(position));
                viewHolder.deleteButton.setOnClickListener(v -> deleteAssignment(position));
            }

            @Override
            public int getItemCount() {
                return assignmentsList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView titleText;
                TextView dueDateText;
                TextView classText;
                Button editButton;
                Button deleteButton;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    titleText = itemView.findViewById(R.id.titleText);
                    dueDateText = itemView.findViewById(R.id.dueDateText);
                    classText = itemView.findViewById(R.id.classText);
                    editButton = itemView.findViewById(R.id.editButton);
                    deleteButton = itemView.findViewById(R.id.deleteButton);
                }
            }
        };

        recyclerView.setAdapter(adapter);

        Button addAssignmentButton = view.findViewById(R.id.addAssignmentButton);
        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAssignmentDialog();
            }
        });

        binding.buttonThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Code for navigating to other fragments or activities

                adapter.notifyDataSetChanged();
            }
        });

        // Load saved assignments if available
        loadSavedAssignments();
    }

    private void showAddAssignmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Assignment");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_assignment, null);
        builder.setView(dialogView);

        final EditText titleInput = dialogView.findViewById(R.id.titleInput);
        final EditText dueDateInput = dialogView.findViewById(R.id.dueDateInput);
        final EditText classInput = dialogView.findViewById(R.id.classInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleInput.getText().toString().trim();
                String dueDate = dueDateInput.getText().toString().trim();
                String associatedClass = classInput.getText().toString().trim();

                if (!title.isEmpty() && !dueDate.isEmpty() && !associatedClass.isEmpty()) {
                    addAssignment(title, dueDate, associatedClass);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showEditAssignmentDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Assignment");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_assignment, null);
        builder.setView(dialogView);

        final EditText titleInput = dialogView.findViewById(R.id.titleInput);
        final EditText dueDateInput = dialogView.findViewById(R.id.dueDateInput);
        final EditText classInput = dialogView.findViewById(R.id.classInput);

        AssignmentModel assignmentModel = assignmentsList.get(position);
        titleInput.setText(assignmentModel.getTitle());
        dueDateInput.setText(assignmentModel.getDueDate());
        classInput.setText(assignmentModel.getAssociatedClass());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleInput.getText().toString().trim();
                String dueDate = dueDateInput.getText().toString().trim();
                String associatedClass = classInput.getText().toString().trim();

                if (!title.isEmpty() && !dueDate.isEmpty() && !associatedClass.isEmpty()) {
                    editAssignment(position, title, dueDate, associatedClass);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addAssignment(String title, String dueDate, String associatedClass) {
        AssignmentModel newAssignment = new AssignmentModel(title, dueDate, associatedClass);
        assignmentsList.add(newAssignment);
        adapter.notifyDataSetChanged();
    }

    private void editAssignment(int position, String title, String dueDate, String associatedClass) {
        AssignmentModel editedAssignment = assignmentsList.get(position);
        editedAssignment.setTitle(title);
        editedAssignment.setDueDate(dueDate);
        editedAssignment.setAssociatedClass(associatedClass);
        adapter.notifyDataSetChanged();
    }

    private void deleteAssignment(int position) {
        assignmentsList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void loadSavedAssignments() {
        // Load saved assignments from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String assignmentsJson = prefs.getString(ASSIGNMENT_LIST_KEY, null);

        if (assignmentsJson != null) {
            // Use Gson to deserialize the JSON string into ArrayList
            Type assignmentListType = new TypeToken<ArrayList<AssignmentModel>>() {}.getType();
            assignmentsList = new Gson().fromJson(assignmentsJson, assignmentListType);

            // Notify the adapter of the data change
            adapter.notifyDataSetChanged();
        }
    }

    private void saveAssignments() {
        // Save assignments to SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Use Gson to serialize the ArrayList into a JSON string
        String assignmentsJson = new Gson().toJson(assignmentsList);

        // Save the JSON string to SharedPreferences
        editor.putString(ASSIGNMENT_LIST_KEY, assignmentsJson);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveAssignments();
        binding = null;
    }
}
