package com.example.tipcalculator_counter;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button clear;
    Button calc;
    RadioButton tenPerc;
    RadioButton fifteenPerc;
    RadioButton twentyPerc;
    RadioButton customPerc;
    EditText party;
    EditText bill;
    EditText tipInput;
    TextView finalTip;
    TextView totalPrice;
    TextView perPersonCost;
    int clearButton;
    float amount;
    int numPeople;
    int tipAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        calc = findViewById(R.id.calculateButton);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });

        party = findViewById(R.id.inputNumPeople);
        party.setOnKeyListener(mKeyListener);
        bill = findViewById(R.id.billAmount);
        bill.setOnKeyListener(mKeyListener);
        tipInput = findViewById(R.id.customTip);
        tipInput.setOnKeyListener(mKeyListener);

        finalTip = findViewById(R.id.tipAmount);
        totalPrice = findViewById(R.id.billTotal);
        perPersonCost = findViewById(R.id.splitCost);

        tenPerc = findViewById(R.id.ten);
        fifteenPerc = findViewById(R.id.fifteen);
        twentyPerc = findViewById(R.id.twenty);
        customPerc = findViewById(R.id.customPercent);

        clearButton = 1;

        if (!bill.getText().toString().equals("") &&
                !party.getText().toString().equals("") &&
                !tipInput.getText().toString().equals("")) {
            if ((Float.parseFloat(bill.getText().toString()) > 1
                    && Integer.parseInt(party.getText().toString()) > 1
                    && Integer.parseInt(tipInput.getText().toString()) > 1)) {
                calc.setEnabled(true);
            }
        } else {
            calc.setEnabled(false);
        }
    }

    private View.OnKeyListener mKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//            if (keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER) {

//            System.out.println("keyCode: " + keyCode);

            switch (view.getId()) {
                case R.id.billAmount:
                    if (keyCode == keyEvent.KEYCODE_ENTER) {
                        if (!bill.getText().toString().equals("")) {
                            if (Float.parseFloat(bill.getText().toString()) < 1) {
                                // show error
                                showErrorAlert("Bill can't be less than $1", R.id.billAmount);
                                calc.setEnabled(false);
//                            System.out.println("bill error");
                            }
                            // else, ?
                            else {
//                                calculate.setEnabled(true);
                                return true;
                            }
                        }
                    }
                    break;
                case R.id.numPeople:
                    if (keyCode == keyEvent.KEYCODE_ENTER) {
                        if (!party.getText().toString().equals("")) {

                            if (Integer.parseInt(party.getText().toString()) < 1) {
                                // show error
                                showErrorAlert("Can't have fewer than 1 person", R.id.numPeople);
                                calc.setEnabled(false);
//                            System.out.println("people error");
                            }
                            // else, ?
                            else {
//                                calculate.setEnabled(true);
                                return true;
                            }
                        }
                    }
                    break;
                case R.id.customTip:
                    if (keyCode == keyEvent.KEYCODE_ENTER) {

                        if (!tipInput.getText().toString().equals("")) {

                            if (Integer.parseInt(tipInput.getText().toString()) < 1) {
                                // show error
                                showErrorAlert("Can't tip less than 1%", R.id.customTip);
                                calc.setEnabled(false);

                            } else {

                                return true;
                            }
                        }
                    }
                    break;
            }
            return false;
        }
    };

    private void showErrorAlert(String errorMessage, final int fieldId) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNeutralButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                findViewById(fieldId).requestFocus();
                            }
                        }).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("button", clearButton);

        String billString = bill.getText().toString();
        Log.d("rotate", billString);
        if (!billString.equals("")) {
            amount = Float.parseFloat(billString);
            savedInstanceState.putFloat("billAmount", amount);
        } else {
            savedInstanceState.putFloat("billAmount", 0);
        }

//        amount = Float.parseFloat(bill.getText().toString());
//        savedInstanceState.putFloat("billAmount", amount);

        String numPeopleText = party.getText().toString();
        if (!numPeopleText.equals("")) {
            numPeople = Integer.parseInt(numPeopleText);
            savedInstanceState.putInt("numPeople", numPeople);
        } else {
            savedInstanceState.putInt("numPeople", 0);
        }
//        numPeople = Integer.parseInt(party.getText().toString());
//        savedInstanceState.putInt("numPeople", numPeople);

        String tipText = tipInput.getText().toString();
        if (!tipText.equals("")) {
            tipAmount = Integer.parseInt(tipText);
            savedInstanceState.putInt("tipAmount", tipAmount);
        } else {
            savedInstanceState.putInt("tipAmount", 0);
        }
//        tipAmount = Integer.parseInt(tip.getText().toString());
//        savedInstanceState.putInt("tipAmount", tipAmount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        clearButton = savedInstanceState.getInt("button");

        amount = savedInstanceState.getFloat("billAmount");
        bill.setText(String.valueOf(amount));

        numPeople = savedInstanceState.getInt("numPeople");
        party.setText(String.valueOf(numPeople));

        tipAmount = savedInstanceState.getInt("tipAmount");
        tipInput.setText(String.valueOf(tipAmount));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.ten:
                if (checked) {
                    clearButton = 1;
                    if (!bill.getText().toString().equals("") &&
                            !party.getText().toString().equals("")) {
                        if ((Float.parseFloat(bill.getText().toString()) >= 1
                                && Integer.parseInt(party.getText().toString()) > 0)) {
                            calc.setEnabled(true);
                        }
                    } else {
                        calc.setEnabled(false);
                    }
                }
                break;
            case R.id.fifteen:
                if (checked) {
                    clearButton = 2;
                    if (!bill.getText().toString().equals("") &&
                            !party.getText().toString().equals("")) {
                        if ((Float.parseFloat(bill.getText().toString()) > 1
                                && Integer.parseInt(party.getText().toString()) > 0)) {
                            calc.setEnabled(true);
                        }
                    } else {
                        calc.setEnabled(false);
                    }
                }
                break;
            case R.id.twenty:
                if (checked) {
                    clearButton = 3;
                    if (!bill.getText().toString().equals("") &&
                            !party.getText().toString().equals("")) {
                        if ((Float.parseFloat(bill.getText().toString()) > 1
                                && Integer.parseInt(party.getText().toString()) > 0)) {
                            calc.setEnabled(true);
                        }
                    } else {
                        calc.setEnabled(false);
                    }
                }
                break;
            case R.id.customPercent:
                if (checked) {
                    clearButton = 4;
                    if (!bill.getText().toString().equals("") &&
                            !party.getText().toString().equals("") &&
                            !tipInput.getText().toString().equals("")) {
                        if ((Float.parseFloat(bill.getText().toString()) > 1
                                && Integer.parseInt(party.getText().toString()) > 0
                                && Integer.parseInt(tipInput.getText().toString()) > 1)) {
                            calc.setEnabled(true);
                        }
                    } else {
                        calc.setEnabled(false);
                    }
                }
                break;
        }
    }

    public void calculate() {
        double billTotal;
        double tempVal;
        double tipPercent = 0;
        double tipAmount;
        double totalPP;
        switch (clearButton) {
            case 1:
//                Log.d("calculate", "case 1");

                tipPercent = 0.1;
                break;
            case 2:
//                Log.d("calculate", "case 2");

                tipPercent = 0.15;
                break;
            case 3:
//                Log.d("calculate", "case 3");

                tipPercent = 0.2;
                break;
            case 4:
//                Log.d("calculate", "case 4");
                tipPercent = Integer.parseInt(tipInput.getText().toString()) / 100.0;
//                Toast toast = Toast.makeText(getApplicationContext(), "custom tip" + String.valueOf(tipPercent), Toast.LENGTH_SHORT);
//                toast.show();
                break;
        }
        tempVal = Float.parseFloat(bill.getText().toString());
        tipAmount = tempVal * tipPercent;
        billTotal = tempVal + tipAmount + 0.0;
        totalPP = billTotal / (Integer.parseInt(party.getText().toString()) + 0.0);

//        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(tipAmount), Toast.LENGTH_SHORT);
//        toast.show();
        finalTip.setText(String.format("Tip: $%.2f", tipAmount));
        totalPrice.setText(String.format("Bill Total: $%.2f", billTotal));
        perPersonCost.setText(String.format("Per Person Cost: $%.2f", totalPP));
    }

    public void reset() {
        bill.setText(null);
        party.setText(null);
        tipInput.setText(null);
        finalTip.setText(null);
        totalPrice.setText(null);
        perPersonCost.setText(null);
    }
}