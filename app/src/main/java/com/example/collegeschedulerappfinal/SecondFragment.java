package com.example.collegeschedulerappfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.example.collegeschedulerappfinal.databinding.FragmentSecondBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String CLASS_LIST_KEY = "classList";
    private FragmentSecondBinding binding;

    private ArrayList<ClassModel> classesList;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load classesList from SharedPreferences
        classesList = loadClassesList();
        if (classesList == null) {
            classesList = new ArrayList<>();  // Initialize the list if it's null
        }

        recyclerView = view.findViewById(R.id.classRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ClassModel classModel = classesList.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;

                viewHolder.courseNameText.setText("Course Name: " + classModel.getClassName());
                viewHolder.timeText.setText("Time: " + classModel.getTime());
                viewHolder.instructorText.setText("Instructor: " + classModel.getInstructor());

                viewHolder.editButton.setOnClickListener(v -> showEditClassDialog(position));
                viewHolder.deleteButton.setOnClickListener(v -> deleteClass(position));
            }

            @Override
            public int getItemCount() {
                return classesList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView courseNameText;
                TextView timeText;
                TextView instructorText;
                Button editButton;
                Button deleteButton;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    courseNameText = itemView.findViewById(R.id.courseNameText);
                    timeText = itemView.findViewById(R.id.timeText);
                    instructorText = itemView.findViewById(R.id.instructorText);
                    editButton = itemView.findViewById(R.id.editButton);
                    deleteButton = itemView.findViewById(R.id.deleteButton);
                }
            }
        };

        recyclerView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showAddClassDialog());

        binding.buttonSecond.setOnClickListener(v -> {
            // Code for navigating to the FirstFragment
            saveClassesList();
        });
    }

    private void showEditClassDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Class");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_class, null);
        builder.setView(dialogView);

        final EditText courseNameInput = dialogView.findViewById(R.id.courseNameInput);
        final EditText timeInput = dialogView.findViewById(R.id.timeInput);
        final EditText instructorInput = dialogView.findViewById(R.id.instructorInput);

        ClassModel classModel = classesList.get(position);
        courseNameInput.setText(classModel.getClassName());
        timeInput.setText(classModel.getTime());
        instructorInput.setText(classModel.getInstructor());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String courseName = courseNameInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String instructor = instructorInput.getText().toString().trim();

            if (!courseName.isEmpty() && !time.isEmpty() && !instructor.isEmpty()) {
                editClass(position, courseName, time, instructor);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void editClass(int position, String courseName, String time, String instructor) {
        ClassModel editedClass = classesList.get(position);
        editedClass.setClassName(courseName);
        editedClass.setTime(time);
        editedClass.setInstructor(instructor);

        adapter.notifyDataSetChanged();

        // Save the updated classesList to SharedPreferences
        saveClassesList();
    }

    private void deleteClass(int position) {
        classesList.remove(position);
        adapter.notifyDataSetChanged();

        // Save the updated classesList to SharedPreferences
        saveClassesList();
    }

    private void showAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Class");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_class, null);
        builder.setView(dialogView);

        final EditText courseNameInput = dialogView.findViewById(R.id.courseNameInput);
        final EditText timeInput = dialogView.findViewById(R.id.timeInput);
        final EditText instructorInput = dialogView.findViewById(R.id.instructorInput);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String courseName = courseNameInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String instructor = instructorInput.getText().toString().trim();

            if (!courseName.isEmpty() && !time.isEmpty() && !instructor.isEmpty()) {
                addClass(courseName, time, instructor);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addClass(String courseName, String time, String instructor) {
        ClassModel newClass = new ClassModel(courseName, time, instructor);
        classesList.add(newClass);
        adapter.notifyDataSetChanged();

        // Save the updated classesList to SharedPreferences
        saveClassesList();
    }

    private void saveClassesList() {
        // Save the classesList to SharedPreferences using Gson
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classesList);
        editor.putString(CLASS_LIST_KEY, json);
        editor.apply();
    }

    private ArrayList<ClassModel> loadClassesList() {
        // Load the classesList from SharedPreferences using Gson
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CLASS_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<ClassModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
