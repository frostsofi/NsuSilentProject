package com.example.table;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private EditText group;
    private Button insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        group = (EditText) findViewById(R.id.editText);

        insert = (Button) findViewById(R.id.button);
    }


    @Override
    public void onStart()
    {
        super.onStart();

        View.OnClickListener onClick = new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                String text = group.getText().toString();
                Toast wrongInput = Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT);

                if (count == 5)
                    finish();

                if (check(text))
                {
                    Intent i = new Intent(MainActivity.super.getBaseContext(), TableActivity.class);
                    i.putExtra("group", text);
                    MainActivity.super.startActivity(i);
                }
                else
                {
                    wrongInput.show();
                }

                count++;
            }
        };

        insert.setOnClickListener(onClick);
    }

    private boolean check (String numOfGroup)
    {
        if (numOfGroup.length() > 7)             //max length of name for group with point and numb (like 19932.3)
            return false;

        Pattern pattern = Pattern.compile("[1][6-9][1-9][0-9][0-9][.]?[1-3]?");

        Matcher matcher = pattern.matcher(numOfGroup);

        return matcher.find();
    }
}
