package com.example.android.justcoffee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


/**
 *  App that displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Check if user wants whipped cream
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        //Check if user wants chocolate topping
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        //Get the user name from the EditText View
        EditText editText = findViewById(R.id.edit_text_input);
        String name = editText.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));   //Only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"max.corvin@hotmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        if(addWhippedCream && addChocolate) {
            basePrice += 3;
        } else if(addChocolate) {
            basePrice += 2;
        } else if(addWhippedCream) {
            basePrice += 1;
        }
        return quantity * basePrice;
    }

    /**
     * Create summary of the order
     *
     * @param name on the order
     * @param price of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not user wants chocolate topping
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method is called when the plus button is clicked
     */
    public void increment(View view) {
        if(quantity == 100) {
            //Show an error message as a toast
            Toast.makeText(this, "You can't have more than 100 coffees.", Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked
     */
    public void decrement(View view) {
        if(quantity == 1) {
            //Show an error message as a toast
            Toast.makeText(this, "You can't have less than 1 coffee.", Toast.LENGTH_SHORT).show();
            //Exit this method early because there is nothing left to do
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }

}