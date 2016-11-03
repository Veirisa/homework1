package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by alexey.nikitin on 13.09.16.
 */

public final class CalculatorActivity extends Activity {

    String st = "";
    double first = 0.0;
    double second = 0.0;
    String mark = "=";
    boolean haveOperation = false;
    boolean started = false;
    TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        text = (TextView) findViewById(R.id.result);
        final Button[] digit = new Button[10];
        digit[0] = (Button) findViewById(R.id.d0);
        digit[1] = (Button) findViewById(R.id.d1);
        digit[2] = (Button) findViewById(R.id.d2);
        digit[3] = (Button) findViewById(R.id.d3);
        digit[4] = (Button) findViewById(R.id.d4);
        digit[5] = (Button) findViewById(R.id.d5);
        digit[6] = (Button) findViewById(R.id.d6);
        digit[7] = (Button) findViewById(R.id.d7);
        digit[8] = (Button) findViewById(R.id.d8);
        digit[9] = (Button) findViewById(R.id.d9);
        Button butClear = (Button) findViewById(R.id.clear);
        final Button butDiv = (Button) findViewById(R.id.div);
        final Button butSub = (Button) findViewById(R.id.sub);
        final Button butAdd = (Button) findViewById(R.id.add);
        final Button butMul = (Button) findViewById(R.id.mul);
        Button butComma = (Button) findViewById(R.id.comma);
        Button butEqv = (Button) findViewById(R.id.eqv);

        View.OnClickListener onClicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 10; i++) {
                    if (v.getId() == digit[i].getId()) {
                        if ((i == 0) && (st.equals("0") || st.equals("-0"))) {
                            break;
                        }
                        if (st.length() < 25)
                            st += Integer.toString(i);
                        text.setText(st);
                        haveOperation = false;
                        started = true;
                        break;
                    }
                }
                switch (v.getId()) {
                    case R.id.clear:
                        st = "";
                        text.setText(st);
                        mark = "=";
                        haveOperation = false;
                        started = false;
                        break;
                    case R.id.comma:
                        if (st.length() == 0) {
                            st = "0.";
                        }
                        if (!st.contains(".")) {
                            st += ".";
                        }
                        text.setText(st);
                        haveOperation = false;
                        started = true;
                        break;
                    case R.id.eqv:
                    case R.id.add:
                    case R.id.sub:
                    case R.id.mul:
                    case R.id.div:
                        if (!started) {
                            break;
                        }
                        Button b = (Button) v;
                        String buf = b.getText().toString();
                        if (haveOperation) {
                            if (!buf.equals("=")) {
                                mark = buf;
                            }
                            break;
                        }
                        if (mark.equals("=")) {
                            if (st.length() > 0 && st.charAt(st.length() - 1) == '.') {
                                st = st.substring(0, st.length() - 1);
                            }
                            if (st.equals("-0")) {
                                st = "0";
                            }
                            text.setText(st);
                        } else {
                            if (st.length() > 0) {
                                second = Double.parseDouble(st);
                            }
                            makeOperation();
                            if (!Double.toString(first).contains("Inf") && !Double.toString(first).contains("NaN")) {
                                first = new BigDecimal(first).setScale(6, RoundingMode.HALF_UP).doubleValue();
                            }
                            st = Double.toString(first);
                            if (st.length() > 2) {
                                if (st.substring(st.length() - 2, st.length()).equals(".0")) {
                                    st = st.substring(0, st.length() - 2);
                                }
                            }
                            if (st.equals("NaN") || st.equals("Infinity") || st.equals("-Infinity")) {
                                text.setText(R.string.error);
                                st = "";
                                mark = "=";
                                started = false;
                                break;
                            } else {
                                if (st.equals("-0")) {
                                    st = "0";
                                }
                            }
                            text.setText(st);
                        }
                        if (mark.equals("=") && st.length() > 0) {
                            first = Double.parseDouble(st);
                        }
                        mark = buf;
                        if (!mark.equals("=")) {
                            haveOperation = true;
                            st = "";
                        }
                        break;
                }
            }
        };
        digit[0].setOnClickListener(onClicker);
        digit[1].setOnClickListener(onClicker);
        digit[2].setOnClickListener(onClicker);
        digit[3].setOnClickListener(onClicker);
        digit[4].setOnClickListener(onClicker);
        digit[5].setOnClickListener(onClicker);
        digit[6].setOnClickListener(onClicker);
        digit[7].setOnClickListener(onClicker);
        digit[8].setOnClickListener(onClicker);
        digit[9].setOnClickListener(onClicker);
        butAdd.setOnClickListener(onClicker);
        butClear.setOnClickListener(onClicker);
        butSub.setOnClickListener(onClicker);
        butMul.setOnClickListener(onClicker);
        butDiv.setOnClickListener(onClicker);
        butEqv.setOnClickListener(onClicker);
        butComma.setOnClickListener(onClicker);
    }

    private void makeOperation() {
        switch (mark) {
            case "+":
                first += second;
                break;
            case "-":
                first -= second;
                break;
            case "*":
                first *= second;
                break;
            case "/":
                first /= second;
                break;
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        first = savedInstanceState.getDouble("first");
        second = savedInstanceState.getDouble("second");
        mark = savedInstanceState.getString("mark");
        st = savedInstanceState.getString("st");
        haveOperation = savedInstanceState.getBoolean("haveOperation");
        started = savedInstanceState.getBoolean("started");
        text.setText(savedInstanceState.getString("saveText"));
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("first", first);
        outState.putDouble("second", second);
        outState.putString("mark", mark);
        outState.putString("st", st);
        outState.putBoolean("haveOperation", haveOperation);
        outState.putBoolean("started", started);
        outState.putString("saveText", text.getText().toString());
    }
}
