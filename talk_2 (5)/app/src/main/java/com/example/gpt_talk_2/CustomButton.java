package com.example.gpt_talk_2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomButton extends LinearLayout {

    private ImageButton imageButton;
    private TextView textView;

    public CustomButton(Context context) {
        super(context);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_button, this, true);

        imageButton = findViewById(R.id.imageButton);
        textView = findViewById(R.id.textView);
    }

    public void setImageResource(int resId) {
        imageButton.setImageResource(resId);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
