package com.foreverrafs.numericals.fragments.ordinary_differential_eqns;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.foreverrafs.numericals.R;
import com.foreverrafs.numericals.adapter.OdeResultsAdapter;
import com.foreverrafs.numericals.core.Numericals;
import com.foreverrafs.numericals.model.OdeResult;
import com.foreverrafs.numericals.utils.Utilities;

import java.util.List;
import java.util.Locale;

import katex.hourglass.in.mathlib.MathView;


/**
 * Created by Aziz Rafsanjani on 11/4/2017.
 */

public class FragmentEulerResults extends Fragment {

    List<OdeResult> results;
    private View rootView;
    private String eqn;
    private double x0, x1, h, initY;
    private int iterations;

    private String getInterval() {
        return String.format(Locale.US, "[%.2f, %.2f]", x0, x1);
    }

    private String getIteration() {
        return String.format(Locale.US, "[%d]", iterations);
    }

    private String getInitY() {
        return String.format(Locale.US, "[%.2f]", initY);
    }

    private String getInitH() {
        return String.format(Locale.US, "[%.2f]", h);
    }


    public void setResults(List<OdeResult> results) {
        this.results = results;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ode_euler_results, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle eqnArgs = getArguments();
        if (eqnArgs != null) {
            this.eqn = eqnArgs.getString("equation");
            this.x0 = eqnArgs.getDouble("x0");
            this.x1 = eqnArgs.getDouble("x1");
            this.iterations = eqnArgs.getInt("iterations");
            this.initY = eqnArgs.getDouble("y");
            this.h = eqnArgs.getDouble("h");
        }

        initControls();
    }

    public void initControls() {
        Button btnBack = rootView.findViewById(R.id.button_back);
        Button btnShowAlgorithm = rootView.findViewById(R.id.button_show_algo);

        MathView equation;
        // String tex = " $$f(x) = 3x^3 + 2x - 5$$";

        equation = rootView.findViewById(R.id.equation);
        equation.setDisplayText(Numericals.generateTexEquation(this.eqn));

        OdeResultsAdapter adapter = new OdeResultsAdapter(results, getContext());
        RecyclerView resultView = rootView.findViewById(R.id.resultList);
        resultView.setLayoutManager(new LinearLayoutManager(getContext()));

        resultView.setAdapter(adapter);

        TextView tvInterval = rootView.findViewById(R.id.interval);
        // TextView tvIterations = rootView.findViewById(R.id.iterations);
        TextView tvInitY = rootView.findViewById(R.id.initY);
        TextView tvH = rootView.findViewById(R.id.h);


        tvInterval.setText(getInterval());
        // tvIterations.setText(getIteration());
        tvInitY.setText(getInitY());
        tvH.setText(getInitH());


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentEuler();
                Bundle eqnArgs = new Bundle();

                //pass eqn and it's paramenters back to the calling fragment
                eqnArgs.putDouble("x0", x0);
                eqnArgs.putDouble("x1", x1);
                eqnArgs.putDouble("y", x1);
                eqnArgs.putInt("iterations", iterations);
                eqnArgs.putDouble("h", h);

                fragment.setArguments(eqnArgs);
                Utilities.replaceFragment(fragment, getFragmentManager(), R.id.fragmentContainer, true);
            }
        });

        btnShowAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.showAlgorithmScreen(getContext(), "euler");
            }
        });

        //Utilities.setTypeFace(rootView.findViewById(R.id.text_header), getContext(), Utilities.TypeFacename.raleway_bold);
    }
}
