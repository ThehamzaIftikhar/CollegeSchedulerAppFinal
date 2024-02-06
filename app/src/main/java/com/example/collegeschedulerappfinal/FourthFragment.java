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

import com.example.collegeschedulerappfinal.databinding.FragmentFourthBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FourthFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String EXAM_LIST_KEY = "examList";
    private FragmentFourthBinding binding;

    private ArrayList<ExamModel> examsList;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFourthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load examsList from SharedPreferences
        examsList = loadExamsList();
        if (examsList == null) {
            examsList = new ArrayList<>();  // Initialize the list if it's null
        }

        recyclerView = view.findViewById(R.id.examRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_exam, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ExamModel examModel = examsList.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;

                viewHolder.examDateText.setText("Exam Date: " + examModel.getExamDate());
                viewHolder.examTimeText.setText("Exam Time: " + examModel.getExamTime());
                viewHolder.examLocationText.setText("Exam Location: " + examModel.getExamLocation());

                viewHolder.editButton.setOnClickListener(v -> showEditExamDialog(position));
                viewHolder.deleteButton.setOnClickListener(v -> deleteExam(position));
            }

            @Override
            public int getItemCount() {
                return examsList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView examDateText;
                TextView examTimeText;
                TextView examLocationText;
                Button editButton;
                Button deleteButton;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    examDateText = itemView.findViewById(R.id.examDateText);
                    examTimeText = itemView.findViewById(R.id.examTimeText);
                    examLocationText = itemView.findViewById(R.id.examLocationText);
                    editButton = itemView.findViewById(R.id.editButton);
                    deleteButton = itemView.findViewById(R.id.deleteButton);
                }
            }
        };

        recyclerView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showAddExamDialog());

        binding.buttonFourth.setOnClickListener(v -> {
            // Code for navigating to the previous fragment
            saveExamsList();
        });
    }

    private void showEditExamDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Exam");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_exam, null);
        builder.setView(dialogView);

        final EditText examDateInput = dialogView.findViewById(R.id.examDateInput);
        final EditText examTimeInput = dialogView.findViewById(R.id.examTimeInput);
        final EditText examLocationInput = dialogView.findViewById(R.id.examLocationInput);

        ExamModel examModel = examsList.get(position);
        examDateInput.setText(examModel.getExamDate());
        examTimeInput.setText(examModel.getExamTime());
        examLocationInput.setText(examModel.getExamLocation());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String examDate = examDateInput.getText().toString().trim();
            String examTime = examTimeInput.getText().toString().trim();
            String examLocation = examLocationInput.getText().toString().trim();

            if (!examDate.isEmpty() && !examTime.isEmpty() && !examLocation.isEmpty()) {
                editExam(position, examDate, examTime, examLocation);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void editExam(int position, String examDate, String examTime, String examLocation) {
        ExamModel editedExam = examsList.get(position);
        editedExam.setExamDate(examDate);
        editedExam.setExamTime(examTime);
        editedExam.setExamLocation(examLocation);

        adapter.notifyDataSetChanged();

        // Save the updated examsList to SharedPreferences
        saveExamsList();
    }

    private void deleteExam(int position) {
        examsList.remove(position);
        adapter.notifyDataSetChanged();

        // Save the updated examsList to SharedPreferences
        saveExamsList();
    }

    private void showAddExamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Exam");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_exam, null);
        builder.setView(dialogView);

        final EditText examDateInput = dialogView.findViewById(R.id.examDateInput);
        final EditText examTimeInput = dialogView.findViewById(R.id.examTimeInput);
        final EditText examLocationInput = dialogView.findViewById(R.id.examLocationInput);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String examDate = examDateInput.getText().toString().trim();
            String examTime = examTimeInput.getText().toString().trim();
            String examLocation = examLocationInput.getText().toString().trim();

            if (!examDate.isEmpty() && !examTime.isEmpty() && !examLocation.isEmpty()) {
                addExam(examDate, examTime, examLocation);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addExam(String examDate, String examTime, String examLocation) {
        ExamModel newExam = new ExamModel(examDate, examTime, examLocation);
        examsList.add(newExam);
        adapter.notifyDataSetChanged();

        // Save the updated examsList to SharedPreferences
        saveExamsList();
    }

    private void saveExamsList() {
        // Save the examsList to SharedPreferences using Gson
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(examsList);
        editor.putString(EXAM_LIST_KEY, json);
        editor.apply();
    }

    private ArrayList<ExamModel> loadExamsList() {
        // Load the examsList from SharedPreferences using Gson
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(EXAM_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<ExamModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
