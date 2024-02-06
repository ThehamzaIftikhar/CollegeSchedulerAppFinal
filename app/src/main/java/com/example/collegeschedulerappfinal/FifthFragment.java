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

import com.example.collegeschedulerappfinal.databinding.FragmentFifthBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FifthFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String TODO_LIST_KEY = "todoList";
    private FragmentFifthBinding binding;

    private ArrayList<ToDoModel> todoList;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFifthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load saved To-Do items if available
        todoList = loadSavedToDoList();
        if (todoList == null) {
            todoList = new ArrayList<>();  // Initialize the list if it's null
        }

        recyclerView = view.findViewById(R.id.todoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todo, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ToDoModel toDoModel = todoList.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;

                viewHolder.taskText.setText(toDoModel.getTask());

                viewHolder.editButton.setOnClickListener(v -> showEditToDoDialog(position));
                viewHolder.deleteButton.setOnClickListener(v -> deleteToDoItem(position));
            }

            @Override
            public int getItemCount() {
                return todoList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView taskText;
                Button editButton;
                Button deleteButton;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    taskText = itemView.findViewById(R.id.taskText);
                    editButton = itemView.findViewById(R.id.editButton);
                    deleteButton = itemView.findViewById(R.id.deleteButton);
                }
            }
        };

        recyclerView.setAdapter(adapter);

        Button addToDoButton = view.findViewById(R.id.addToDoButton);
        addToDoButton.setOnClickListener(v -> showAddToDoDialog());

    }

    private void showAddToDoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add To-Do Item");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_todo, null);
        builder.setView(dialogView);

        final EditText taskInput = dialogView.findViewById(R.id.taskInput);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String task = taskInput.getText().toString().trim();
            if (!task.isEmpty()) {
                addToDoItem(task);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditToDoDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit To-Do Item");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_todo, null);
        builder.setView(dialogView);

        final EditText taskInput = dialogView.findViewById(R.id.taskInput);

        ToDoModel toDoModel = todoList.get(position);
        taskInput.setText(toDoModel.getTask());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String task = taskInput.getText().toString().trim();
            if (!task.isEmpty()) {
                editToDoItem(position, task);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addToDoItem(String task) {
        ToDoModel newToDoItem = new ToDoModel(task);
        todoList.add(newToDoItem);
        adapter.notifyDataSetChanged();
        saveToDoList();  // Save the updated todoList to SharedPreferences
    }

    private void editToDoItem(int position, String task) {
        ToDoModel editedToDoItem = todoList.get(position);
        editedToDoItem.setTask(task);
        adapter.notifyDataSetChanged();
        saveToDoList();  // Save the updated todoList to SharedPreferences
    }

    private void deleteToDoItem(int position) {
        todoList.remove(position);
        adapter.notifyDataSetChanged();
        saveToDoList();  // Save the updated todoList to SharedPreferences
    }

    private ArrayList<ToDoModel> loadSavedToDoList() {
        // Load saved To-Do items from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String todoListJson = prefs.getString(TODO_LIST_KEY, null);

        if (todoListJson != null) {
            // Use Gson to deserialize the JSON string into ArrayList
            Type todoListType = new TypeToken<ArrayList<ToDoModel>>() {}.getType();
            return new Gson().fromJson(todoListJson, todoListType);
        } else {
            return null;
        }
    }

    private void saveToDoList() {
        // Save To-Do items to SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Use Gson to serialize the ArrayList into a JSON string
        String todoListJson = new Gson().toJson(todoList);

        // Save the JSON string to SharedPreferences
        editor.putString(TODO_LIST_KEY, todoListJson);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
