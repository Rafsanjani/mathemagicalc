package com.foreverrafs.numericals.ui.location_of_roots;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.foreverrafs.core.LocationOfRootResult;
import com.foreverrafs.numericals.R;
import com.foreverrafs.numericals.activities.MainActivity;
import com.foreverrafs.numericals.utils.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import timber.log.Timber;

//All fragments which will solve a location of root problem must extend from this Fragment and assign
//appropriate values to the protected field members else a NullPointerException will be thrown when
//any of the protected methods attempts to execute in the subclasses
public abstract class FragmentRootBase extends Fragment {
    private static final String TAG = "FragmentRootBase";
    protected List<LocationOfRootResult> roots = null;
    protected View rootView;
    protected ViewGroup parentContainer;
    private View.OnKeyListener myKeyListener;
    private Button btnCalculate;

    protected NavController navController = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        initControls();
    }

    protected void onEquationChanged() {
        TextView tvAnswer = rootView.findViewById(R.id.tvAnswer);
        btnCalculate = rootView.findViewById(R.id.btnCalculate);
        btnCalculate.setText(getResources().getString(R.string.calculate));
        Utilities.animateAnswer(tvAnswer, parentContainer, Utilities.DisplayMode.HIDE);
    }

    abstract protected void initControls();

    protected void registerOnKeyListener(final TextInputLayout... inputLayouts) throws RuntimeException {
        if (inputLayouts.length == 0) {
            Timber.e("At least one inputLayout must be supplied to registerOnKeyListener");
            return;
        }
        myKeyListener = (view, i, keyEvent) -> {
            onEquationChanged();
            for (TextInputLayout inputLayout : inputLayouts) {
                inputLayout.setErrorEnabled(false);
            }

            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return false;

            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                onCalculate(btnCalculate.getText().toString());
                return true;
            }
            return false;
        };

        for (TextInputLayout inputLayout : inputLayouts) {
            inputLayout.getEditText().setOnKeyListener(myKeyListener);
        }
    }

    protected void showAlgorithm(String functionName) {
        NavController navController = Navigation.findNavController(rootView);
        if (getActivity() != null)
            ((MainActivity) getActivity()).showAlgorithm(navController, functionName);
    }

    protected void onBackClicked(Button button) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).goToMainMenu();
        }
    }

    protected void unRegisterOnKeyListeners(@NonNull final TextInputLayout... inputLayouts) throws RuntimeException {
        if (myKeyListener == null) {
            throw new RuntimeException("You must call register before calling unregister");
        }
        for (TextInputLayout inputLayout : inputLayouts) {
            inputLayout.getEditText().setOnKeyListener(null);
        }
    }


    protected boolean validateInput(@NonNull final TextInputLayout... inputLayouts) {
        if (inputLayouts.length == 0) {
            Timber.e("At least one inputLayout must be supplied to registerOnKeyListener");
            return true;
        }

        boolean validated = true;
        for (TextInputLayout inputLayout : inputLayouts) {
            if (inputLayout.getEditText().getText().toString().isEmpty()) {
                inputLayout.setErrorEnabled(true);
                if (inputLayout.getId() == R.id.til_user_input) {
                    inputLayout.setError("Cannot be empty!");
                } else {
                    inputLayout.setError("?");
                }
                validated = false;
            }
        }

        return validated;
    }

    protected abstract void onCalculate(@NonNull final String buttonText);
}
