package com.example.collegeschedulerappfinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.appcompat.app.AppCompatActivity;


import com.example.collegeschedulerappfinal.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    TextView inputtedCourse, inputtedTime, inputtedInstructor;
    Button submitButton, previous;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputtedCourse = view.findViewById(R.id.CourseNameTextView);
        inputtedCourse.setVisibility(View.INVISIBLE);

        inputtedTime = view.findViewById(R.id.CourseTimeTextView);
        inputtedTime.setVisibility(View.INVISIBLE);

        inputtedInstructor = view.findViewById(R.id.CourseInstructorTextView);
        inputtedInstructor.setVisibility(View.INVISIBLE);

        submitButton = view.findViewById(R.id.SubmitClasses);
        submitButton.setVisibility(View.INVISIBLE);

        previous = view.findViewById(R.id.button_second);

        //PREVIOUS BUTTON
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        //SUBMIT
        binding.SubmitClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 inputtedCourse = getView().findViewById(R.id.CourseNameTextView);
                 inputtedTime = getView().findViewById(R.id.CourseTimeTextView);
                 inputtedInstructor = getView().findViewById(R.id.CourseInstructorTextView);

                inputtedCourse.setVisibility(View.INVISIBLE);
                inputtedTime.setVisibility(View.INVISIBLE);
                inputtedInstructor.setVisibility(View.INVISIBLE);
                submitButton.setVisibility(View.INVISIBLE);
                previous.setVisibility(View.VISIBLE);

            }
        });

        //ADD NEW CLASSES
        binding.addClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputtedCourse.setVisibility(View.VISIBLE);
                inputtedTime.setVisibility(View.VISIBLE);
                inputtedInstructor.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
                previous.setVisibility(View.INVISIBLE);

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
