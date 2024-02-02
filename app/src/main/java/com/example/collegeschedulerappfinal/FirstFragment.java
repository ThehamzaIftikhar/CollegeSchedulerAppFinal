package com.example.collegeschedulerappfinal;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.appcompat.app.AppCompatActivity;


import com.example.collegeschedulerappfinal.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.gray));
                actionBar.setBackgroundDrawable(colorDrawable);
            }
        }

        // Card for "Classes"
        binding.cardClasses.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment));

        // Card for "Assignments"
        binding.cardAssignments.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_FirstFragment_to_ThirdFragment));

        // Exams button
        binding.cardExams.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_FirstFragment_to_FourthFragment));

        // To Do List button
        binding.cardToDoList.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_FirstFragment_to_FifthFragment));

        // Reminders button
        // Add a click listener for your "Reminders" button if needed.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
