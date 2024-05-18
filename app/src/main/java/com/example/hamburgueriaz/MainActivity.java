package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int quantity = 0;
    private final int HAMBURGER_PRICE = 20;
    private final int BACON_PRICE = 2;
    private final int CHEESE_PRICE = 2;
    private final int ONION_RINGS_PRICE = 3;

    private EditText etName;
    private CheckBox cbBacon, cbCheese, cbOnionRings;
    private TextView tvItemQuantity, tvTotalPrice;
    private Button buttonDecrement, buttonIncrement, buttonFinishOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.ETName);
        cbBacon = findViewById(R.id.CBBacon);
        cbCheese = findViewById(R.id.CBQueijo);
        cbOnionRings = findViewById(R.id.CBOnionRings);
        tvItemQuantity = findViewById(R.id.tvItemQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        buttonDecrement = findViewById(R.id.button_decrement);
        buttonIncrement = findViewById(R.id.button_increment);
        buttonFinishOrder = findViewById(R.id.ButtonFinishOrder);

        buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractQuantity();
            }
        });

        buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity();
            }
        });

        buttonFinishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOrder();
            }
        });
    }

    private void addQuantity() {
        quantity++;
        updateQuantity();
    }

    private void subtractQuantity() {
        if (quantity > 0) {
            quantity--;
            updateQuantity();
        } else {
            Toast.makeText(this, "Quantidade não pode ser negativa", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuantity() {
        tvItemQuantity.setText(String.valueOf(quantity));
    }

    private void finishOrder() {
        String name = etName.getText().toString();
        boolean hasBacon = cbBacon.isChecked();
        boolean hasCheese = cbCheese.isChecked();
        boolean hasOnionRings = cbOnionRings.isChecked();

        int totalPrice = calculateTotalPrice(hasBacon, hasCheese, hasOnionRings);

        String orderSummary = createOrderSummary(name, hasBacon, hasCheese, hasOnionRings, totalPrice);
        tvTotalPrice.setText(orderSummary);

        sendEmail(name, orderSummary);
    }

    private int calculateTotalPrice(boolean hasBacon, boolean hasCheese, boolean hasOnionRings) {
        int basePrice = HAMBURGER_PRICE;

        if (hasBacon) {
            basePrice += BACON_PRICE;
        }

        if (hasCheese) {
            basePrice += CHEESE_PRICE;
        }

        if (hasOnionRings) {
            basePrice += ONION_RINGS_PRICE;
        }

        return basePrice * quantity;
    }

    private String createOrderSummary(String name, boolean hasBacon, boolean hasCheese, boolean hasOnionRings, int price) {
        String orderSummary = "Nome do cliente: " + name;
        orderSummary += "\nTem Bacon? " + (hasBacon ? "Sim" : "Não");
        orderSummary += "\nTem Queijo? " + (hasCheese ? "Sim" : "Não");
        orderSummary += "\nTem Onion Rings? " + (hasOnionRings ? "Sim" : "Não");
        orderSummary += "\nQuantidade: " + quantity;
        orderSummary += "\nPreço final: R$ " + price;
        return orderSummary;
    }

    private void sendEmail(String name, String orderSummary) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + name);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Não há aplicativo de e-mail instalado", Toast.LENGTH_SHORT).show();
        }
    }
}
