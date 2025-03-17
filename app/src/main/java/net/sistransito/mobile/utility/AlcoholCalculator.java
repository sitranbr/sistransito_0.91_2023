package net.sistransito.mobile.utility;

import android.widget.EditText;

import net.sistransito.mobile.ait.AitData;

public class AlcoholCalculator {

    private static final double UPPER_LIMIT_FOR_CRIME = 0.032;
    private static final double LOWER_LIMIT = 0.05;
    private static final double MIDDLE_LIMIT = 0.50;
    private static final double UPPER_LIMIT = 2.0;
    private static final double AMOUNT_ALCOHOL_TOLERATED = 0.00;

    private String unitMeasureCalculation;

    public AlcoholCalculator(String unitMeasureCalculation) {
        this.unitMeasureCalculation = unitMeasureCalculation;
    }

    public boolean calculateAmountAlcoholInBlood(String value, EditText etValueConsidered, AitData aitData) {
        try {
            double amountAlcoholTolerated = AMOUNT_ALCOHOL_TOLERATED;
            double consideredValue = Double.valueOf(value);

            switch (Double.compare(consideredValue, LOWER_LIMIT)) {
                case -1:
                    etValueConsidered.setText(String.format("%.2f", Math.floor(amountAlcoholTolerated * 100) / 100));
                    break;
                case 0:
                case 1:
                    if (consideredValue <= MIDDLE_LIMIT) {
                        amountAlcoholTolerated = consideredValue - UPPER_LIMIT_FOR_CRIME;
                    } else if (consideredValue <= UPPER_LIMIT) {
                        amountAlcoholTolerated = consideredValue - (consideredValue * 8) / 100;
                    } else {
                        amountAlcoholTolerated = consideredValue - (consideredValue * 30) / 100;
                    }
                    etValueConsidered.setText(String.format("%.2f", Math.floor(amountAlcoholTolerated * 100) / 100) + " " + unitMeasureCalculation);
                    aitData.setValueConsidered(String.format("%.2f", Math.floor(amountAlcoholTolerated * 100) / 100) + " " + unitMeasureCalculation);
                    break;
                default:
                    break;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

