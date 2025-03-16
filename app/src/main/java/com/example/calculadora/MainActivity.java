package com.example.calculadora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TextView tvOperation, tvResult;
    private SwitchMaterial themeSwitch;
    private CalculatorPagerAdapter  pagerAdapter;
    private CalculatorLogic calculatorLogic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize calculator logic
        calculatorLogic = new CalculatorLogic();

        // Initialize UI components
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tvOperation = findViewById(R.id.tvOperation);
        tvResult = findViewById(R.id.tvResult);
        themeSwitch = findViewById(R.id.themeSwitch);

        // Setup ViewPager with fragments
        pagerAdapter = new CalculatorPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.standard);
                    break;
                case 1:
                    tab.setText(R.string.scientific);
                    break;
                case 2:
                    tab.setText(R.string.programmer);
                    break;
            }
        }).attach();
        // Listen for tab changes
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                calculatorLogic.setCalculatorMode(tab.getPosition());
                updateDisplay();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Theme switch listener
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Theme switching code would go here
            // Using AppCompatDelegate.setDefaultNightMode() or similar
        });

        // Initialize display
        updateDisplay();
    }
    public void updateDisplay() {
        tvOperation.setText(calculatorLogic.getCurrentOperation());
        tvResult.setText(calculatorLogic.getCurrentResult());
    }
    public void onNumberClick(String number) {
        calculatorLogic.appendNumber(number);
        updateDisplay();
    }

    public void onOperatorClick(String operator) {
        calculatorLogic.applyOperator(operator);
        updateDisplay();
    }

    public void onFunctionClick(String function) {
        calculatorLogic.applyFunction(function);
        updateDisplay();
    }

    public void onEqualsClick() {
        calculatorLogic.calculate();
        updateDisplay();
    }

    public void onClearClick() {
        calculatorLogic.clear();
        updateDisplay();
    }

    public void onDecimalClick() {
        calculatorLogic.appendDecimal();
        updateDisplay();
    }

    public void onParenthesisClick() {
        calculatorLogic.appendParenthesis();
        updateDisplay();
    }

    public void onBaseChange(int base) {
        calculatorLogic.setBase(base);
        updateDisplay();
    }

    public void onMemoryAction(String action) {
        calculatorLogic.handleMemory(action);
        updateDisplay();
    }

    // ViewPager adapter
    private static class CalculatorPagerAdapter extends FragmentStateAdapter {
        public CalculatorPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new StandardCalculatorFragment();
                case 1:
                    return new ScientificCalculatorFragment();
                case 2:
                    return new ProgrammerCalculatorFragment();
                default:
                    return new StandardCalculatorFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    // Standard Calculator Fragment
    public static class StandardCalculatorFragment extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_standard, container, false);
            setupButtons(view);
            return view;
        }

        private void setupButtons(View view) {
            MainActivity activity = (MainActivity) requireActivity();

            // Setup number buttons
            for (int i = 0; i <= 9; i++) {
                int id = getResources().getIdentifier("btn" + i, "id", requireContext().getPackageName());
                Button btn = view.findViewById(id);
                final String number = String.valueOf(i);
                btn.setOnClickListener(v -> activity.onNumberClick(number));
            }

            // Setup operator buttons
            Map<Integer, String> operators = new HashMap<>();
            operators.put(R.id.btnAdd, "+");
            operators.put(R.id.btnSubtract, "-");
            operators.put(R.id.btnMultiply, "×");
            operators.put(R.id.btnDivide, "÷");

            for (Map.Entry<Integer, String> entry : operators.entrySet()) {
                Button btn = view.findViewById(entry.getKey());
                final String operator = entry.getValue();
                btn.setOnClickListener(v -> activity.onOperatorClick(operator));
            }

            // Setup function buttons
            Button btnEquals = view.findViewById(R.id.btnEquals);
            btnEquals.setOnClickListener(v -> activity.onEqualsClick());

            Button btnClear = view.findViewById(R.id.btnClear);
            btnClear.setOnClickListener(v -> activity.onClearClick());

            Button btnDecimal = view.findViewById(R.id.btnDecimal);
            btnDecimal.setOnClickListener(v -> activity.onDecimalClick());

            Button btnPercent = view.findViewById(R.id.btnPercent);
            btnPercent.setOnClickListener(v -> activity.onFunctionClick("%"));

            Button btnParentheses = view.findViewById(R.id.btnParentheses);
            btnParentheses.setOnClickListener(v -> activity.onParenthesisClick());
        }
    }

    // Scientific Calculator Fragment
    public static class ScientificCalculatorFragment extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_cientifica, container, false);
            setupButtons(view);
            return view;
        }

        private void setupButtons(View view) {
            MainActivity activity = (MainActivity) requireActivity();

            // Setup number buttons
            for (int i = 0; i <= 9; i++) {
                int id = getResources().getIdentifier("btn" + i, "id", requireContext().getPackageName());
                Button btn = view.findViewById(id);
                if (btn != null) {
                    final String number = String.valueOf(i);
                    btn.setOnClickListener(v -> activity.onNumberClick(number));
                }
            }

            // Setup operator buttons
            Map<Integer, String> operators = new HashMap<>();
            operators.put(R.id.btnAdd, "+");
            operators.put(R.id.btnSubtract, "-");
            operators.put(R.id.btnMultiply, "×");
            operators.put(R.id.btnDivide, "÷");
            operators.put(R.id.btnPower, "^");

            for (Map.Entry<Integer, String> entry : operators.entrySet()) {
                Button btn = view.findViewById(entry.getKey());
                if (btn != null) {
                    final String operator = entry.getValue();
                    btn.setOnClickListener(v -> activity.onOperatorClick(operator));
                }
            }

            // Setup function buttons
            Map<Integer, String> functions = new HashMap<>();
            functions.put(R.id.btnSin, "sin");
            functions.put(R.id.btnCos, "cos");
            functions.put(R.id.btnTan, "tan");
            functions.put(R.id.btnLn, "ln");
            functions.put(R.id.btnLog, "log");
            functions.put(R.id.btnSquareRoot, "sqrt");
            functions.put(R.id.btnPi, "π");
            functions.put(R.id.btnFactorial, "!");

            for (Map.Entry<Integer, String> entry : functions.entrySet()) {
                Button btn = view.findViewById(entry.getKey());
                if (btn != null) {
                    final String function = entry.getValue();
                    btn.setOnClickListener(v -> activity.onFunctionClick(function));
                }
            }

            // Setup other buttons
            Button btnEquals = view.findViewById(R.id.btnEquals);
            if (btnEquals != null) {
                btnEquals.setOnClickListener(v -> activity.onEqualsClick());
            }

            Button btnClear = view.findViewById(R.id.btnClear);
            if (btnClear != null) {
                btnClear.setOnClickListener(v -> activity.onClearClick());
            }

            Button btnDecimal = view.findViewById(R.id.btnDecimal);
            if (btnDecimal != null) {
                btnDecimal.setOnClickListener(v -> activity.onDecimalClick());
            }

            Button btnParenOpen = view.findViewById(R.id.btnParenOpen);
            if (btnParenOpen != null) {
                btnParenOpen.setOnClickListener(v -> activity.onFunctionClick("("));
            }

            Button btnParenClose = view.findViewById(R.id.btnParenClose);
            if (btnParenClose != null) {
                btnParenClose.setOnClickListener(v -> activity.onFunctionClick(")"));
            }

            // Setup memory buttons
            Button btnMemoryStore = view.findViewById(R.id.btnMemoryStore);
            if (btnMemoryStore != null) {
                btnMemoryStore.setOnClickListener(v -> activity.onMemoryAction("MS"));
            }

            Button btnMemoryRecall = view.findViewById(R.id.btnMemoryRecall);
            if (btnMemoryRecall != null) {
                btnMemoryRecall.setOnClickListener(v -> activity.onMemoryAction("MR"));
            }

            // Setup mode buttons
            Button btnRad = view.findViewById(R.id.btnRad);
            if (btnRad != null) {
                btnRad.setOnClickListener(v -> activity.onFunctionClick("RAD"));
            }

            Button btnDeg = view.findViewById(R.id.btnDeg);
            if (btnDeg != null) {
                btnDeg.setOnClickListener(v -> activity.onFunctionClick("DEG"));
            }
        }
    }

    // Programmer Calculator Fragment
    public static class ProgrammerCalculatorFragment extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_programador, container, false);
            setupButtons(view);
            return view;
        }

        private void setupButtons(View view) {
            MainActivity activity = (MainActivity) requireActivity();

            // Setup number buttons (0-9)
            for (int i = 0; i <= 9; i++) {
                int id = getResources().getIdentifier("btn" + i, "id", requireContext().getPackageName());
                Button btn = view.findViewById(id);
                if (btn != null) {
                    final String number = String.valueOf(i);
                    btn.setOnClickListener(v -> activity.onNumberClick(number));
                }
            }

            // Setup hex buttons (A-F)
            for (char c = 'A'; c <= 'F'; c++) {
                int id = getResources().getIdentifier("btn" + c, "id", requireContext().getPackageName());
                Button btn = view.findViewById(id);
                if (btn != null) {
                    final String hex = String.valueOf(c);
                    btn.setOnClickListener(v -> activity.onNumberClick(hex));
                }
            }

            // Setup operator buttons
            Map<Integer, String> operators = new HashMap<>();
            operators.put(R.id.btnAdd, "+");
            operators.put(R.id.btnSubtract, "-");
            operators.put(R.id.btnMultiply, "×");
            operators.put(R.id.btnDivide, "÷");
            operators.put(R.id.btnMod, "%");
            operators.put(R.id.btnAnd, "AND");
            operators.put(R.id.btnOr, "OR");
            operators.put(R.id.btnXor, "XOR");
            operators.put(R.id.btnNot, "NOT");
            operators.put(R.id.btnShiftLeft, "<<");
            operators.put(R.id.btnShiftRight, ">>");

            for (Map.Entry<Integer, String> entry : operators.entrySet()) {
                Button btn = view.findViewById(entry.getKey());
                if (btn != null) {
                    final String operator = entry.getValue();
                    btn.setOnClickListener(v -> activity.onOperatorClick(operator));
                }
            }

            // Setup function buttons
            Button btnEquals = view.findViewById(R.id.btnEquals);
            if (btnEquals != null) {
                btnEquals.setOnClickListener(v -> activity.onEqualsClick());
            }

            Button btnClear = view.findViewById(R.id.btnClear);
            if (btnClear != null) {
                btnClear.setOnClickListener(v -> activity.onClearClick());
            }

            // Setup radio buttons for base selection
            RadioGroup radioGroup = view.findViewById(R.id.radioGroupNumeralSystem);
            if (radioGroup != null) {
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (checkedId == R.id.radioHex) {
                        activity.onBaseChange(16);
                        enableHexButtons(view, true);
                    } else if (checkedId == R.id.radioDec) {
                        activity.onBaseChange(10);
                    }
                });
            }
        }
    }
}