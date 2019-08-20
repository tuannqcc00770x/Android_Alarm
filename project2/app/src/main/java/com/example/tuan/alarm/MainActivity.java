package com.example.tuan.alarm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //constants
    public static final int REQUEST_CODE_ADD = 1;
    public static final int RESULT_CODE_ADD = 2;
    public static final int REQUEST_CODE_EDIT = 3;
    public static final int RESULT_CODE_EDIT = 4;

    //declaration
    ArrayList<Integer> arrayId = new ArrayList<Integer>();
    ListView listView;
    AlarmClockAdapter adapter;
    int currentPosition = 0;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ArrayList<AlarmClock> arrayAlarm;
    Calendar calendar;
    int soundId = 0;
    DataBase dataBase;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase("alarm.sqlite");
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        init();
        callData();
    }

    //Method to set id for item and pending intent
    private int generateId(){
        Random rd = new Random();
        int number = rd.nextInt();
        boolean similar = false;
        if (arrayId!=null){
                for (int i=0; i<arrayId.size();i++){
                    if (number == arrayId.get(i)){
                        similar = true;
                        break;
                    }
            }
        } else {
            arrayId.add(number);
            return number;
        }
        if (similar == true){
            generateId();
        } else {
            arrayId.add(number);
            return number;
        }
        return number;
    }

    //init view on screen
    private void init(){
        ImageView imgAdd = findViewById(R.id.menu_add);
        imgAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        startActivityForResult(intent,REQUEST_CODE_ADD);
                    }
                }
        );
        ImageView imgRingtone = findViewById(R.id.menu_ringtone);
        imgRingtone.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = findViewById(R.id.menu_ringtone);
                        PopupMenu popup = new PopupMenu(MainActivity.this, view);
                        popup.getMenuInflater().inflate(R.menu.ringmenu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        switch (item.getItemId()){
                                            case R.id.ring1:
                                                soundId = 0;
                                                editor.putInt("soundId", soundId);
                                                editor.commit();
                                                break;
                                            case R.id.ring2:
                                                soundId = 1;
                                                editor.putInt("soundId", soundId);
                                                editor.commit();
                                                break;
                                            default:
                                                break;
                                        }
                                        return false;
                                    }
                                }
                        );
                    }
                }
        );
        listView = findViewById(R.id.lv_alarm);
        arrayAlarm = new ArrayList<AlarmClock>();
        adapter = new AlarmClockAdapter(this, R.layout.alarm_clock, arrayAlarm);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        PopupMenu popup = new PopupMenu(MainActivity.this, view);
                        popup.getMenuInflater().inflate(R.menu.editmenu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()){
                                            case R.id.delete:
                                                dataBase.queryData("DELETE FROM alarm WHERE id = '"+arrayAlarm.get(position).getId()+"'");
                                                if (arrayAlarm.get(position).getToggle()==true){
                                                    removeAlarm(arrayAlarm.get(position));
                                                }
                                                arrayAlarm.remove(position);
                                                adapter.notifyDataSetChanged();
                                                break;
                                            case R.id.edit:
                                                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                                startActivityForResult(intent,REQUEST_CODE_EDIT);
                                                currentPosition = position;
                                                break;
                                            default:
                                                break;
                                        }
                                        return false;
                                    }
                                }
                        );
                        return false;
                    }
                }
        );
    }

    //Call saved data
    public void callData(){
        sharedPreferences = getSharedPreferences("soundId",MODE_PRIVATE);
        soundId = sharedPreferences.getInt("soundId",0);
        dataBase = new DataBase(this, "alarm.sqlite", null,1, null);
        dataBase.queryData("CREATE TABLE IF NOT EXISTS alarm(id INTEGER PRIMARY KEY, hour VARCHAR(2), minute VARCHAR(2), ampm VARCHAR(2), toggle INTEGER)");
        Cursor cursor = dataBase.getData("SELECT * FROM alarm");
        arrayAlarm.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String hour = cursor.getString(1);
            String minute = cursor.getString(2);
            String ampm = cursor.getString(3);
            int toggle = cursor.getInt(4);
            if (toggle == 1){
                arrayAlarm.add(new AlarmClock(hour,minute,ampm,true,id));
            } else {
                arrayAlarm.add(new AlarmClock(hour,minute,ampm,false,id));
            }
            setAlarm(arrayAlarm.get(arrayAlarm.size()-1));
        }
    }

    //set alarm for item
    public void  setAlarm(AlarmClock alarmClock){
        if(alarmClock.getToggle()==true){
            String hour = alarmClock.getHour();
            String minute = alarmClock.getMinute();
            String ampm = alarmClock.getAmpm();
            if (ampm.equals("PM") && !hour.equals("12") ){
                hour = ""+(Integer.parseInt(hour)+12);
            }
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
            calendar.set(Calendar.SECOND,59);
            calendar.set(Calendar.MILLISECOND,999);
            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                calendar.set(Calendar.SECOND,0);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                calendar.set(Calendar.SECOND,0);
            }
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.putExtra("switch","on");
            intent.putExtra("sound",soundId);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, alarmClock.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent );
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    //1000 * 60 *60*3, pendingIntent);
        }
    }

    //cancel alarm for item
    public void removeAlarm(AlarmClock alarmClock){
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("switch","off");
        intent.putExtra("sound",soundId);
        sendBroadcast(intent);
        for (int i = 0; i<arrayId.size();i++){//remove id in array
            if (alarmClock.getId() == arrayId.get(i)) {
                arrayId.remove(i);
                break;
            }
        }
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, alarmClock.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);//overide pending intent, then cancel.
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }


    //receive and solve information from other activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD) {
            switch(resultCode) {
                case RESULT_CODE_ADD:
                    Bundle bundleAdd = data.getBundleExtra(AddActivity.ADD_BUNDLE);
                    arrayAlarm.add(new AlarmClock(bundleAdd.getString(AddActivity.ADD_HOUR),bundleAdd.getString(AddActivity.ADD_MINUTE),
                            bundleAdd.getString(AddActivity.ADD_AMPM),true, generateId()));
                    adapter.notifyDataSetChanged();
                    setAlarm(arrayAlarm.get(arrayAlarm.size()-1));
                    dataBase.queryData("INSERT INTO alarm VALUES('"+arrayAlarm.get(arrayAlarm.size()-1).getId()+"','"+bundleAdd.getString(AddActivity.ADD_HOUR)+"','"+bundleAdd.getString(AddActivity.ADD_MINUTE)+"'," +
                            "'"+bundleAdd.getString(AddActivity.ADD_AMPM)+"', 1)");
                    break;
                default:
                    break;
            }
        }
        if(requestCode==REQUEST_CODE_EDIT) {
            switch(resultCode) {
                case RESULT_CODE_EDIT:
                    if (arrayAlarm.get(currentPosition).getToggle()==true){
                        removeAlarm(arrayAlarm.get(currentPosition));
                    }
                    Bundle bundleEdit = data.getBundleExtra(EditActivity.EDIT_BUNDLE);
                    arrayAlarm.get(currentPosition).setHour(bundleEdit.getString(EditActivity.EDIT_HOUR));
                    arrayAlarm.get(currentPosition).setMinute(bundleEdit.getString(EditActivity.EDIT_MINUTE));
                    arrayAlarm.get(currentPosition).setAmpm(bundleEdit.getString(EditActivity.EDIT_AMPM));
                    arrayAlarm.get(currentPosition).setToggle(true);
                    dataBase.queryData("UPDATE alarm SET hour='"+arrayAlarm.get(currentPosition).getHour()+"'WHERE id ='"+arrayAlarm.get(currentPosition).getId()+"' ");
                    dataBase.queryData("UPDATE alarm SET minute='"+arrayAlarm.get(currentPosition).getMinute()+"'WHERE id ='"+arrayAlarm.get(currentPosition).getId()+"' ");
                    dataBase.queryData("UPDATE alarm SET ampm='"+arrayAlarm.get(currentPosition).getAmpm()+"'WHERE id ='"+arrayAlarm.get(currentPosition).getId()+"' ");
                    if (arrayAlarm.get(currentPosition).getToggle()==true){
                        dataBase.queryData("UPDATE alarm SET toggle=1 WHERE id ='"+arrayAlarm.get(currentPosition).getId()+"' ");
                    } else {
                        dataBase.queryData("UPDATE alarm SET toggle=0 WHERE id ='"+arrayAlarm.get(currentPosition).getId()+"' ");
                    }
                    setAlarm(arrayAlarm.get(currentPosition));
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }
}
