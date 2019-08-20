package com.example.tuan.alarm;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;

public class EditActivity extends AppCompatActivity {
    //constants
    public static final String EDIT_HOUR = "EDIT_HOUR";
    public static final String EDIT_MINUTE = "EDIT_MINUTE";
    public static final String EDIT_AMPM = "EDIT_AMPM";
    public static final String EDIT_BUNDLE = "EDIT_BUNDLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        edit();
        //back to main activity when press "<"
        ImageView editBack = findViewById(R.id.edit_back);
        editBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );
    }

    //action when chosing "edit" on context menu
    public void edit(){
        final TimePicker timePicker = findViewById(R.id.timepickerEdit);
        Button btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour, minute, ampm;
                if (timePicker.getCurrentHour()>11){
                    hour = "" + (timePicker.getCurrentHour()-12);
                    ampm = "PM";
                } else {
                    hour = "" + timePicker.getCurrentHour();
                    ampm = "AM";
                }
                if (timePicker.getCurrentMinute()<10){
                    minute = "0" + timePicker.getCurrentMinute();
                } else {
                    minute = "" + timePicker.getCurrentMinute();
                }
                if (hour.equals("0") && ampm.equals("PM") ){
                    hour = ""+12;
                }
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(EDIT_HOUR,hour);
                bundle.putString(EDIT_MINUTE,minute);
                bundle.putString(EDIT_AMPM, ampm);
                intent.putExtra(EDIT_BUNDLE, bundle);
                setResult(MainActivity.RESULT_CODE_EDIT,intent);
                finish();
            }
        });
    }
}
