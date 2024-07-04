package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText queryEditText;
    private ImageView sendQuery, logo, appIcon;
    private FloatingActionButton btnShowDialog;
    private ProgressBar progressBar;
    private LinearLayout chatResponse;
    private ChatFutures chatModel;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.message_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }

        sendQuery = dialog.findViewById(R.id.sendMessage);
        queryEditText = dialog.findViewById(R.id.queryEditText);
        btnShowDialog = findViewById(R.id.showMessageDialog);
        progressBar = findViewById(R.id.progressBar2);
        chatResponse = findViewById(R.id.chatResponse);
        appIcon = findViewById(R.id.appIcon);

        btnShowDialog.setOnClickListener(v -> dialog.show());

        sendQuery.setOnClickListener(v -> {
            dialog.dismiss();
            progressBar.setVisibility(View.VISIBLE);
            appIcon.setVisibility(View.GONE);

            String query = queryEditText.getText().toString();
            queryEditText.setText("");

            addChatMessage("You", query, getDrawable(R.drawable.user));

            GeminiResp.getResponse(chatModel, query, new ResponseCallback() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.GONE);
                    addChatMessage("AI", response, getDrawable(R.drawable.ai));
                }

                @Override
                public void onError(Throwable throwable) {
                    addChatMessage("AI", "Please try again.", getDrawable(R.drawable.ai));
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        chatModel = getChatModel();
    }

    private ChatFutures getChatModel() {
        GeminiResp model = new GeminiResp();
        GenerativeModelFutures modelFutures = model.getModel();
        return modelFutures.startChat();
    }

    private void addChatMessage(String userName, String messageText, Drawable image) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_message, null);
        TextView name = view.findViewById(R.id.name);
        TextView message = view.findViewById(R.id.agentMessage);
        ImageView imageView = view.findViewById(R.id.logo);

        name.setText(userName);
        message.setText(messageText);
        imageView.setImageDrawable(image);
        chatResponse.addView(view);

        ScrollView scrollView = findViewById(R.id.scrollView2);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
