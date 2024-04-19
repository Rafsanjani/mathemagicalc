package com.foreverrafs.numericals.ui.sys_of_equations;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foreverrafs.core.Numericals;
import com.foreverrafs.numericals.databinding.FragmentGaussianComplete3x3Binding;
import com.foreverrafs.numericals.databinding.LayoutMatrix3x3Binding;
import com.foreverrafs.numericals.utils.Utilities;

import timber.log.Timber;

/**
 * Created by Aziz Rafsanjani on 11/4/2017.
 */

public class FragmentGaussianComplete3x3 extends FragmentSystemOfEquationsBase implements View.OnKeyListener, TextWatcher {

    FragmentGaussianComplete3x3Binding binding = null;
    LayoutMatrix3x3Binding matrixBinding = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGaussianComplete3x3Binding.inflate(inflater, container, false);
        matrixBinding = binding.layoutMatrix;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        binding.btnBackToMainMenu.setOnClickListener(view -> goToMainmenu());

        binding.btnCalculate.setOnClickListener(view -> {
            Timber.i("solving the system using gaussian with Complete pivoting");
            onCalculate();
        });
    }

    private void onCalculate() {
        if (getMatrices()) {
            Utilities.animateAnswer(matrixBinding.solutionMatrix, binding.parentContainer, Utilities.DisplayMode.SHOW);
            Utilities.animateAnswer(matrixBinding.solutionMatrix2, binding.parentContainer, Utilities.DisplayMode.SHOW);
            Utilities.animateAnswer(matrixBinding.solHeader1, binding.parentContainer, Utilities.DisplayMode.SHOW);
            Utilities.animateAnswer(matrixBinding.solHeader2, binding.parentContainer, Utilities.DisplayMode.SHOW);
        } else {
            Toast.makeText(getContext(), "Error with input", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean getMatrices() {
        EditText[][] etA = new EditText[3][3];
        double[][] a = new double[3][3];

        EditText[] etB = new EditText[3];
        double[] b = new double[3];

        etA[0][0] = matrixBinding.a11;
        etA[0][1] = matrixBinding.a12;
        etA[0][2] = matrixBinding.a13;
        etA[1][0] = matrixBinding.a21;
        etA[1][1] = matrixBinding.a22;
        etA[1][2] = matrixBinding.a23;
        etA[2][0] = matrixBinding.a31;
        etA[2][1] = matrixBinding.a32;
        etA[2][2] = matrixBinding.a33;

        etB[0] = matrixBinding.b1;
        etB[1] = matrixBinding.b2;
        etB[2] = matrixBinding.b3;


        Utilities.hideKeyboard(etA[0][0]);

        for (int i = 0; i < etA.length; i++) {
            for (int j = 0; j < etA.length; j++) {
                etA[i][j].addTextChangedListener(this);
                try {
                    a[i][j] = Double.parseDouble(etA[i][j].getText().toString());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
            try {
                etB[i].addTextChangedListener(this);
                b[i] = Double.parseDouble(etB[i].getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }


        //get the solution matrix
        double[] solution = Numericals.gaussianWithCompletePivoting(a, b);


        //our previous matrices have been mutated so we can represent them on the textviews
        TextView[][] tvA = new TextView[3][3];
        tvA[0][0] = matrixBinding.sa11;
        tvA[0][1] = matrixBinding.sa12;
        tvA[0][2] = matrixBinding.sa13;
        tvA[1][0] = matrixBinding.sa21;
        tvA[1][1] = matrixBinding.sa22;
        tvA[1][2] = matrixBinding.sa23;
        tvA[2][0] = matrixBinding.sa31;
        tvA[2][1] = matrixBinding.sa32;
        tvA[2][2] = matrixBinding.sa33;


        TextView[] tvX = new TextView[3];
        TextView[] tvB = new TextView[3];

        tvB[0] = matrixBinding.sab1;
        tvB[1] = matrixBinding.sab2;
        tvB[2] = matrixBinding.sab3;

        tvX[0] = matrixBinding.sax1;
        tvX[1] = matrixBinding.sax2;
        tvX[2] = matrixBinding.sax3;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                tvA[i][j].setText(String.valueOf(a[i][j]));
            }
            tvX[i].setText(String.valueOf(solution[i]));
            tvB[i].setText(String.valueOf(b[i]));
        }
        return true;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Utilities.animateAnswer(matrixBinding.solutionMatrix, binding.parentContainer, Utilities.DisplayMode.HIDE);
        Utilities.animateAnswer(matrixBinding.solutionMatrix2, binding.parentContainer, Utilities.DisplayMode.HIDE);
        Utilities.animateAnswer(matrixBinding.solHeader1, binding.parentContainer, Utilities.DisplayMode.HIDE);
        Utilities.animateAnswer(matrixBinding.solHeader2, binding.parentContainer, Utilities.DisplayMode.HIDE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
