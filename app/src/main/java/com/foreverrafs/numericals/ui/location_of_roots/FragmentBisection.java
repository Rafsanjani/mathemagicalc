package com.foreverrafs.numericals.ui.location_of_roots;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foreverrafs.core.LocationOfRootResult;
import com.foreverrafs.core.Numericals;
import com.foreverrafs.numericals.R;
import com.foreverrafs.numericals.databinding.FragmentLocOfRootsBisectionBinding;
import com.foreverrafs.numericals.utils.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import timber.log.Timber;


/**
 * Created by Aziz Rafsanjani on 11/4/2017.
 */

public class FragmentBisection extends FragmentRootBase implements TextWatcher {
    private static final String TAG = "FragmentBisection";

    private List<LocationOfRootResult> roots = null;
    private TextWatcher etToleranceTextWatcher = null;
    private TextWatcher etIterationsTextWatcher = null;

    TextInputLayout tilX0;

    TextInputLayout tilX1;

    TextInputLayout tilIterations;

    TextInputLayout tilTolerance;

    TextInputLayout tilEquation;

    TextView tvAnswer;

    Button btnCalculate;

    FragmentLocOfRootsBisectionBinding binding;

    private EditText etIterations, etX0, etX1, etTolerance, etEquation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocOfRootsBisectionBinding.inflate(inflater);
        btnCalculate = binding.btnCalculate;
        tvAnswer = binding.tvAnswer;
        tilEquation = binding.tilUserInput;
        tilTolerance = binding.tilTolerance;
        tilIterations = binding.tilIterations;
        tilX0 = binding.tilX0;
        tilX1 = binding.tilX1;
        parentContainer = binding.parentContainer;
        rootView = binding.getRoot();

        return binding.getRoot();
    }


    @Override
    public void initControls() {
        //initialize EditTexts
        etEquation = tilEquation.getEditText();
        etTolerance = tilTolerance.getEditText();
        etIterations = tilIterations.getEditText();
        etX0 = tilX0.getEditText();
        etX1 = tilX1.getEditText();

        registerOnKeyListener(tilEquation, tilIterations, tilTolerance, tilX0, tilX1);

        etIterationsTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //get the tolerance value based on the number of iterations
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onEquationChanged();
                try {
                    etTolerance.removeTextChangedListener(etToleranceTextWatcher);
                    int iterations = Integer.parseInt(etIterations.getText().toString());
                    double x0, x1;

                    x0 = Double.parseDouble(etX0.getText().toString());
                    x1 = Double.parseDouble(etX1.getText().toString());

                    double tolerance = Numericals.getBisectionTolerance(iterations, x0, x1);

                    etTolerance.setText(String.valueOf(tolerance));
                } catch (NumberFormatException ex) {
                    Timber.i("Initial guesses are not provided");
                } finally {
                    etTolerance.addTextChangedListener(etToleranceTextWatcher);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etToleranceTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //get the number of iterations based on the tolerance value
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onEquationChanged();
                try {
                    etIterations.removeTextChangedListener(etIterationsTextWatcher);
                    double tolerance = Double.parseDouble(etTolerance.getText().toString());

                    if (tolerance == 0) {
                        return;
                    }

                    double x0, x1;

                    x0 = Double.parseDouble(etX0.getText().toString());
                    x1 = Double.parseDouble(etX1.getText().toString());

                    int iterations = Numericals.getBisectionIterations(tolerance, x0, x1);

                    etIterations.setText(String.valueOf(iterations));
                } catch (NumberFormatException ex) {
                    Timber.i("Initial guesses are not provided");
                } finally {
                    etIterations.addTextChangedListener(etIterationsTextWatcher);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etIterations.addTextChangedListener(etIterationsTextWatcher);
        etTolerance.addTextChangedListener(etToleranceTextWatcher);
        etEquation.addTextChangedListener(this);

        btnCalculate.setOnClickListener(v -> onCalculateClicked(btnCalculate));
        binding.btnShowAlgo.setOnClickListener(v -> onShowAlgoClicked());
    }


    void onCalculateClicked(Button button) {
        onCalculate(button.getText().toString());
    }

    void onShowAlgoClicked() {
        showAlgorithm("bisection");
    }

    @Override
    protected void onCalculate(@NonNull final String buttonText) {
        //only handle empty inputs in this module and display using their corresponding TextInputLayouts.
        //Any other errors are handled in Numericals.java. This may check most of the NumberFormatException which
        //gets thrown as a result of passing empty parameters to Type.ParseType(string param)
        if (!validateInput(tilEquation, tilX1, tilX0, tilTolerance, tilIterations)) {
            return;
        }

        String eqn;
        float x0, x1, tol;
        int iter;

        try {
            eqn = etEquation.getText().toString().toLowerCase();
            x0 = Float.parseFloat(etX0.getText().toString());
            x1 = Float.parseFloat(etX1.getText().toString());
            tol = Float.parseFloat(etTolerance.getText().toString());
            iter = Integer.parseInt(etIterations.getText().toString());
        } catch (NumberFormatException ex) {
            tilEquation.setErrorEnabled(true);
            tilEquation.setError("One or more of the input expressions are invalid!");
            Timber.i("Error parsing one or more of the expressions");
            return;
        }

        //are we displaying all answers or just the last iteration
        if (buttonText.equals(getResources().getString(R.string.calculate))) {
            try {
                roots = Numericals.bisect(eqn, x0, x1, iter, tol);
            } catch (Exception ex) {
                tilEquation.setErrorEnabled(true);
                tilEquation.setError(ex.getMessage());
                return;
            }

            //get the last item from the roots and display in single mode to the user
            double root = roots.get(roots.size() - 1).getX3();

            tvAnswer.setText(String.valueOf(root));

            //animate the answer into view
            Utilities.animateAnswer(tvAnswer, parentContainer, Utilities.DisplayMode.SHOW);
        } else if (buttonText.equals(getResources().getString(R.string.show_iterations))) {
            navController.navigate(FragmentBisectionDirections.fragmentBisectionResults(eqn, x0, x1, iter, tol,
                    roots.toArray(new LocationOfRootResult[0])));
        }
        btnCalculate.setText(getResources().getString(R.string.show_iterations));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onEquationChanged();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onEquationChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        onEquationChanged();
    }
}

