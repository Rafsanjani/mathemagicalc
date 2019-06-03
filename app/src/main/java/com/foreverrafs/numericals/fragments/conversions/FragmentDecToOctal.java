package com.foreverrafs.numericals.fragments.conversions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.foreverrafs.numericals.R;
import com.foreverrafs.numericals.activities.MainActivity;
import com.foreverrafs.numericals.core.Numericals;
import com.foreverrafs.numericals.utils.Utilities;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
//import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * Created by Aziz Rafsanjani on 11/4/2017.
 */

public class FragmentDecToOctal extends Fragment implements View.OnClickListener, TextWatcher {

    View rootView;
    TextInputLayout tilUserInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dec_to_octal, container, false);
        //("Decimal Calculator", "Convert decimals to binary");

        initControls();
        return rootView;
    }

    private void initControls() {
        TextView tvAnswer = rootView.findViewById(R.id.text_answer_binary);

        Utilities.setTypeFace(tvAnswer, getContext(), Utilities.TypeFaceName.falling_sky);
        ////Utilities.setTypeFace(rootView.findViewById(R.id.text_header), getContext(), Utilities.TypeFacename.raleway_bold);

        Button btnBack = rootView.findViewById(R.id.button_back);
        Button btnCalculate = rootView.findViewById(R.id.button_calculate);

        TextInputEditText etInput = rootView.findViewById(R.id.text_user_input);
        tilUserInput = rootView.findViewById(R.id.til_user_input);


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

            /*case R.id.show_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View detailsView = View.inflate(getContext(), R.layout.number_conversion_details, null);


                builder.setView(detailsView)
                        .create();
                builder.show();

                ExpandableTextView expTv1 = detailsView.findViewById(R.id.expand_text_view);
                expTv1.setText(rawBinary);
                break;*/
        }
    }


    private void onCalculate() {
        EditText etInput = rootView.findViewById(R.id.text_user_input);
        TextView tvAnswer = rootView.findViewById(R.id.text_answer_octal);

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
                //Toast.makeText(getContext(), "Number should be greater than 0", Toast.LENGTH_LONG).show();
                return;
            }

            String octalDecimal = Numericals.DecimalToOctal(decimal);

            tvAnswer.setText(octalDecimal);

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
            Utilities.animateAnswer(rootView.findViewById(R.id.text_answer_octal),
                    rootView.findViewById(R.id.parentContainer), Utilities.DisplayMode.HIDE);
        }
    }
}
