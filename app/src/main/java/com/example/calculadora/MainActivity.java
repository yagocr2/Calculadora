package com.example.calculadora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
            View view = inflater.inflate(R.layout.fragment_standard, container, false);
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
            btnParentheses.setOnClickListener(v -> {
                activity.onParenthesisClick();
                activity.updateDisplay();
            });
        }
    }

    // Scientific Calculator Fragment
    public static class ScientificCalculatorFragment extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_cientifica, container, false);
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
                btnParenOpen.setOnClickListener(v -> {
                    activity.calculatorLogic.appendParenthesis();
                    activity.updateDisplay();
                });
            }

            Button btnParenClose = view.findViewById(R.id.btnParenClose);
            if (btnParenClose != null) {
                btnParenClose.setOnClickListener(v -> activity.onFunctionClick(")"));
            }

            // Setup memory buttons
            Button btnMemoryStore = view.findViewById(R.id.btnMemoryStore);
            if (btnMemoryStore != null) {
                btnParenClose.setOnClickListener(v -> {
                    activity.calculatorLogic.appendParenthesis();
                    activity.updateDisplay();
                });
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
            View view = inflater.inflate(R.layout.fragment_programador, container, false);
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
                        enableHexButtons(view, false);
                    } else if (checkedId == R.id.radioOct) {
                        activity.onBaseChange(8);
                        enableHexButtons(view, false);
                    } else if (checkedId == R.id.radioBin) {
                        activity.onBaseChange(2);
                        enableHexButtons(view, false);
                    }
                });

            }
        }
        private int currentBase;
        private void syncBaseWithLogic() {
            MainActivity activity = (MainActivity) requireActivity();
            this.currentBase = activity.calculatorLogic.getCurrentBase();
        }
        private void enableHexButtons(View view, boolean enable) {
            // Habilitar/deshabilitar botones A-F
            for (char c = 'A'; c <= 'F'; c++) {
                int buttonId = getResources().getIdentifier("btn" + c, "id", requireContext().getPackageName());
                Button hexButton = view.findViewById(buttonId);

                if (hexButton != null) {
                    hexButton.setEnabled(enable);
                    // Cambiar apariencia según estado
                    hexButton.setAlpha(enable ? 1.0f : 0.5f);
                }
            }

            // Actualizar botones numéricas según la base
            int[] digitButtons = {R.id.btn8, R.id.btn9};
            for (int id : digitButtons) {
                Button btn = view.findViewById(id);
                if (btn != null) {
                    btn.setEnabled(currentBase > 8);
                    btn.setAlpha(currentBase > 8 ? 1.0f : 0.5f);
                }
            }
        }
    }
    public class CalculatorLogic {
        private String currentOperation = "";
        private String currentResult = "0";
        private int calculatorMode = 0; // 0: Standard, 1: Scientific, 2: Programmer
        private int currentBase = 10;
        private boolean isRadians = true;
        private double memoryValue = 0;

        public void appendDecimal() {
            if (!currentResult.contains(".")) {
                currentResult += ".";
            }
        }
        public void appendParenthesis() {
            // Lógica para balancear paréntesis
            // Contar paréntesis existentes para balancear
            long openCount = currentOperation.chars().filter(ch -> ch == '(').count();
            long closeCount = currentOperation.chars().filter(ch -> ch == ')').count();

            if (openCount == closeCount) {
                currentResult += "(";
            } else {
                currentResult += ")";
            }

            updateDisplay();
        }
        public void appendNumber(String number) {
            if (calculatorMode == 2 && !isValidForBase(number)) return;

            if (currentResult.equals("0") && !number.equals(".")) {
                currentResult = number;
            } else {
                currentResult += number;
            }
        }

        public void applyOperator(String operator) {
            // Si hay un paréntesis abierto, no limpiar la operación
            if (currentOperation.replaceAll("[^(]", "").length() >
                    currentOperation.replaceAll("[^)]", "").length()) {
                currentOperation += currentResult + " " + operator + " ";
            } else {
                if (!currentOperation.isEmpty()) {
                    calculate();
                }
                currentOperation = currentResult + " " + operator + " ";
            }
            currentResult = "0";
        }

        public void calculate() {
            try {
                // Evaluar expresión con paréntesis
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
                String expression = currentOperation.replaceAll("[×]", "*")
                        .replaceAll("[÷]", "/")
                        + currentResult;

                double result = (Double) engine.eval(expression);
                currentResult = String.valueOf(result);
                currentOperation = "";
            } catch (Exception e) {
                currentResult = "Error";
            }
        }

        public void applyFunction(String function) {
            try {
                double value = Double.parseDouble(currentResult);
                switch (function) {
                    case "sin":
                        value = isRadians ? Math.sin(value) : Math.sin(Math.toRadians(value));
                        break;
                    case "cos":
                        value = isRadians ? Math.cos(value) : Math.cos(Math.toRadians(value));
                        break;
                    case "tan":
                        value = isRadians ? Math.tan(value) : Math.tan(Math.toRadians(value));
                        break;
                    case "ln": value = Math.log(value); break;
                    case "log": value = Math.log10(value); break;
                    case "sqrt": value = Math.sqrt(value); break;
                    case "!": value = factorial(value); break;
                }
                currentResult = String.valueOf(value);
            } catch (Exception e) {
                currentResult = "Error";
            }
        }

        private double factorial(double n) {
            if (n == 0) return 1;
            return n * factorial(n - 1);
        }

        public void setBase(int base) {
            this.currentBase = base;
            // Implementar conversión de bases aquí
        }

        public void handleMemory(String action) {
            switch (action) {
                case "MS": memoryValue = Double.parseDouble(currentResult); break;
                case "MR": currentResult = String.valueOf(memoryValue); break;
            }
        }

        // Getters y Setters
        public String getCurrentOperation() { return currentOperation; }
        public String getCurrentResult() { return currentResult; }
        public void setCalculatorMode(int mode) { this.calculatorMode = mode; }
        public int getCurrentBase() { return currentBase; }
        public void clear() { currentOperation = ""; currentResult = "0"; }

        private boolean isValidForBase(String input) {
            try {
                Integer.parseInt(input, currentBase);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}