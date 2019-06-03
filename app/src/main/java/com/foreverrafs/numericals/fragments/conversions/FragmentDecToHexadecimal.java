package com.foreverrafs.numericals.fragments.conversions;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.foreverrafs.numericals.R;
import com.foreverrafs.numericals.activities.MainActivity;
import com.foreverrafs.numericals.activities.ShowAlgorithm;
import com.foreverrafs.numericals.core.Numericals;
import com.foreverrafs.numericals.utils.Utilities;
//import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * Created by Aziz Rafsanjani on 11/4/2017.
 */

public class FragmentDecToHexadecimal extends Fragment implements View.OnClickListener, TextWatcher {

    View rootView;
    TextInputLayout tilUserInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dec_to_hexadecimal, container, false);
        //("Decimal Calculator", "Convert decimals to binary");

        initControls();
        return rootView;
    }

    private void initControls() {
        //Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/FallingSky.otf");


        TextView tvAnswer = rootView.findViewById(R.id.text_answer_binary);

        tilUserInput = rootView.findViewById(R.id.til_user_input);
        TextInputEditText etInput = rootView.findViewById(R.id.text_user_input);

        ////Utilities.setTypeFace(rootView.findViewById(R.id.text_header), getContext(), Utilities.TypeFacename.raleway_bold);
        Utilities.setTypeFace(tvAnswer, getContext(), Utilities.TypeFaceName.falling_sky);

        Button btnBack = rootView.findViewById(R.id.button_back);
        Button btnCalculate = rootView.findViewById(R.id.button_calculate);


        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                tilUserInput.setErrorEnabled(false);
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    onCalculate();
                    return true;
                }
                return false;
            }
        });

        etInput.addTextChangedListener(this);

        btnBack.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);
        rootView.findViewById(R.id.button_show_algo).setOnClickListener(this);
    }

    private void onShowAlgorithm() {
        Bundle bundle = new Bundle();
        bundle.putString("algorithm_name", "dectohexa");

        startActivity(new Intent(getContext(), ShowAlgorithm.class).putExtras(bundle));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                Utilities.replaceFragment(new FragmentConversionsMenu(), getFragmentManager(), R.id.fragmentContainer, true);
                break;

            case R.id.button_calculate:
                onCalculate();
                break;

            case R.id.button_show_algo:
                onShowAlgorithm();
                break;
        }
    }


    private void onCalculate() {
        TextInputEditText etInput = rootView.findViewById(R.id.text_user_input);
        TextView tvAnswer = rootView.findViewById(R.id.text_answer_hexadecimal);

        String decimal = etInput.getText().toString();
        if (decimal.isEmpty()) {
            tilUserInput.setErrorEnabled(true);
            tilUserInput.setError("Input cannot be empty!");
            //Toast.makeText(getContext(), "Input field is empty", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            double decLong = Double.parseDouble(decimal);

            if (decLong <= 0) {
                tilUserInput.setErrorEnabled(true);
                tilUserInput.setError("Should be greater than 0!");
                // Toast.makeText(getContext(), "Number should be greater than 0", Toast.LENGTH_LONG).show();
                return;
            }

            String hexadecimal = Numericals.DecimalToHexadecimal(decimal);

            tvAnswer.setText(hexadecimal);

            Utilities.animateAnswer(rootView.findViewById(R.id.layout_answer_area),
                    (ViewGroup) rootView.findViewById(R.id.parentContainer), Utilities.DisplayMode.SHOW);


        } catch (NumberFormatException ex) {
            Log.e(Utilities.LOG_TAG, "cannot parse " + decimal + " to an integer value");
        } catch (Exception ex) {
            Log.e(Utilities.LOG_TAG, ex.getMessage());
        } finally {
            MainActivity.hideKeyboard(etInput);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 0) {
            Utilities.animateAnswer(rootView.findViewById(R.id.text_answer_hexadecimal),
                    rootView.findViewById(R.id.parentContainer), Utilities.DisplayMode.HIDE);
        }
    }
}
