package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import javax.script.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAC, btnDEL, btnParen,
            btnDiv, btnMul, btnPlus, btnSub, btnRes, btnCom, btnPercent;

    private int totalParentheses = 0;
    private boolean dotUsed = false;
    private final static int IS_NUMBER = 0;
    private final static int IS_OPERATOR = 1;
    private final static int IS_OPEN_PARENTHESIS = 2;
    private final static int IS_CLOSE_PARENTHESIS = 3;
    private final static int IS_DOT = 4;
    TextView tvInput, tvResult;
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("rhino");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViewVariables();
        setOnClickListeners();

    }

    private void initializeViewVariables() {
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);

        btnAC = (Button) findViewById(R.id.btnAC);
        btnDEL = (Button) findViewById(R.id.btnDEL);
        btnParen = (Button) findViewById(R.id.btnParen);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        btnMul = (Button) findViewById(R.id.btnMul);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnRes = (Button) findViewById(R.id.btnRes);
        btnCom = (Button) findViewById(R.id.btnCom);
        btnPercent = (Button) findViewById(R.id.btnPercent);
        tvInput = (TextView) findViewById(R.id.textView);
        tvResult = (TextView) findViewById(R.id.editText);
    }

    private void setOnClickListeners() {
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        btnDEL.setOnClickListener(this);
        btnAC.setOnClickListener(this);
        btnCom.setOnClickListener(this);
        btnRes.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnParen.setOnClickListener(this);
        btnPercent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn0:
                addNumber("0");
                break;
            case R.id.btn1:
                addNumber("1");
                break;
            case R.id.btn2:
                addNumber("2");
                break;
            case R.id.btn3:
                addNumber("3");
                break;
            case R.id.btn4:
                addNumber("4");
                break;
            case R.id.btn5:
                addNumber("5");
                break;
            case R.id.btn6:
                addNumber("6");
                break;
            case R.id.btn7:
                addNumber("7");
                break;
            case R.id.btn8:
                addNumber("8");
                break;
            case R.id.btn9:
                addNumber("9");
                break;
            case R.id.btnPlus:
                addOperator("+");
                break;
            case R.id.btnSub:
                addOperator("-");
                break;
            case R.id.btnMul:
                addOperator("×");
                break;
            case R.id.btnDiv:
                addOperator("÷");
                break;
            case R.id.btnPercent:
                addOperator("%");
                break;
            case R.id.btnCom:
                addDot();
                break;
            case R.id.btnParen:
                addParentheses();
                break;
            case R.id.btnAC:
                tvInput.setText("");
                tvResult.setText("");
                totalParentheses = 0;
                dotUsed = false;
                break;
            case R.id.btnDEL:
                if (!tvInput.getText().toString().equals(""))
                    tvInput.setText(tvInput.getText().toString().substring(0, tvInput.getText().length() - 1));
                break;
            case R.id.btnRes:
                if (!tvInput.getText().toString().equals(""))
                    calculate(tvInput.getText().toString());
                break;
        }
    }

    private boolean addDot() {
        boolean checked = false;
        if (tvInput.getText().length() == 0) {
            tvInput.setText("0.");
            dotUsed = true;
            checked = true;
        } else {
            if (!dotUsed) {
                if (checkLastCharacter(tvInput.getText().charAt(tvInput.getText().length() - 1) + "") == IS_OPERATOR) {
                    tvInput.setText(tvInput.getText() + "0.");
                    checked = true;
                    dotUsed = true;
                } else if (checkLastCharacter(tvInput.getText().charAt(tvInput.getText().length() - 1) + "") == IS_NUMBER) {
                    tvInput.setText(tvInput.getText() + ".");
                    checked = true;
                    dotUsed = true;
                }
            }
        }
        return checked;
    }

    private boolean addParentheses() {
        boolean checked = false;
        int operationLength = tvInput.getText().length();

        if (operationLength == 0) {
            tvInput.setText(tvInput.getText() + "(");
            dotUsed = false;
            totalParentheses++;
            checked = true;
        } else if (totalParentheses > 0 && operationLength > 0) {
            String lastInput = tvInput.getText().charAt(operationLength - 1) + "";
            switch (checkLastCharacter(lastInput)) {
                case IS_NUMBER:
                case IS_CLOSE_PARENTHESIS:
                    tvInput.setText(tvInput.getText() + ")");
                    checked = true;
                    totalParentheses--;
                    dotUsed = false;
                    break;
                case IS_OPERATOR:
                case IS_OPEN_PARENTHESIS:
                    tvInput.setText(tvInput.getText() + "(");
                    checked = true;
                    totalParentheses++;
                    dotUsed = false;
                    break;
            }
        } else if (totalParentheses == 0 && operationLength > 0) {
            String lastInput = tvInput.getText().charAt(operationLength - 1) + "";
            if (checkLastCharacter(lastInput) == IS_OPERATOR) {
                tvInput.setText(tvInput.getText() + "(");
                checked = true;
                dotUsed = false;
                totalParentheses++;
            } else {
                tvInput.setText(tvInput.getText() + "x(");
                checked = true;
                dotUsed = false;
                totalParentheses++;
            }
        }
        return checked;
    }

    private boolean addOperator(String operator) {
        boolean checked = false;
        int operationLength = tvInput.getText().length();
        if (operationLength > 0) {
            String lastInput = tvInput.getText().charAt(operationLength - 1) + "";

            if ((lastInput.equals("+") || lastInput.equals("-") || lastInput.equals("×") || lastInput.equals("÷") || lastInput.equals("%"))) {
                Toast.makeText(getApplicationContext(), "Wrong format", Toast.LENGTH_LONG).show();
            } else if (operator.equals("%") && checkLastCharacter(lastInput) == IS_NUMBER) {
                tvInput.setText(tvInput.getText() + operator);
                dotUsed = false;
                checked = true;
            } else if (!operator.equals("%")) {
                tvInput.setText(tvInput.getText() + operator);
                dotUsed = false;
                checked = true;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Format. operator Without any numbers?", Toast.LENGTH_LONG).show();
        }
        return checked;
    }

    private boolean addNumber(String number) {
        boolean checked = false;
        int operationLength = tvInput.getText().length();
        if (operationLength > 0) {
            String lastCharacter = tvInput.getText().charAt(operationLength - 1) + "";
            int lastCharacterState = checkLastCharacter(lastCharacter);

            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter.equals("0")) {
                tvInput.setText(number);
                checked = true;
            } else if (lastCharacterState == IS_OPEN_PARENTHESIS) {
                tvInput.setText(tvInput.getText() + number);
                checked = true;
            } else if (lastCharacterState == IS_CLOSE_PARENTHESIS || lastCharacter.equals("%")) {
                tvInput.setText(tvInput.getText() + "×" + number);
                checked = true;
            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERATOR || lastCharacterState == IS_DOT) {
                tvInput.setText(tvInput.getText() + number);
                checked = true;
            }
        } else {
            tvInput.setText(tvInput.getText() + number);
            checked = true;
        }
        return checked;
    }

    private int checkLastCharacter(String lastCharacter) {
        try {
            Integer.parseInt(lastCharacter);
            return IS_NUMBER;
        } catch (NumberFormatException ignored) {
        }
        if ((lastCharacter.equals("+") || lastCharacter.equals("-") || lastCharacter.equals("×")
                || lastCharacter.equals("÷") || lastCharacter.equals("%")))
            return IS_OPERATOR;
        if (lastCharacter.equals("("))
            return IS_OPEN_PARENTHESIS;
        if (lastCharacter.equals(")"))
            return IS_CLOSE_PARENTHESIS;
        if (lastCharacter.equals("."))
            return IS_DOT;
        return -1;
    }

    private void calculate(String input) {
        String result = "";
        try {
            String temp = input;

            temp = temp.replaceAll("%", "/100").replaceAll("×", "*").replaceAll("÷", "/");
            result = scriptEngine.eval(temp).toString();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            BigDecimal decimal = new BigDecimal(result);
            result = decimal.setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString();

            if (result.equals("Infinity")) { //  2/0
                tvInput.setText(input);
            } else if (result.contains(".")) { //  125.0 = 125
                result = result.replaceAll("\\.?0*$", "");
                tvResult.setText(result);
            }
        } catch (Exception e) {
            tvResult.setText("undefined");
            Toast.makeText(getApplicationContext(), "Wrong Format", Toast.LENGTH_SHORT).show();
//            return;
        }
    }
}

