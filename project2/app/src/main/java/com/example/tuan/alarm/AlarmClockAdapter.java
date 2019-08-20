package com.example.tuan.alarm;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.List;

public class AlarmClockAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private List<AlarmClock> alarmList;

    public AlarmClockAdapter(Context context, int layout, List<AlarmClock> alarmList) {
        this.context = context;
        this.layout = layout;
        this.alarmList = alarmList;
    }

    private class ViewHolder{
    TextView time, alarm, ampm;
    ToggleButton btn;
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder = new ViewHolder();
            holder.time = convertView.findViewById(R.id.tv_time);
            holder.ampm = convertView.findViewById(R.id.tv_ampm);
            holder.alarm = convertView.findViewById(R.id.tv_alarmm);
            holder.btn = convertView.findViewById(R.id.btn_toggle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final AlarmClock alarmClock = alarmList.get(position);
        holder.time.setText(alarmClock.getHour()+":"+alarmClock.getMinute());
        holder.ampm.setText(alarmClock.getAmpm());
        holder.alarm.setText("Alarm");
        holder.btn.setChecked(((MainActivity) context).arrayAlarm.get(position).getToggle());
        holder.btn.setOnCheckedChangeListener(
               new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if (isChecked) {
                           alarmClock.setToggle(true);
                           ((MainActivity)context).setAlarm(((MainActivity) context).arrayAlarm.get(position));//set alarm in main activity when press toggle button "ON"
                           ((MainActivity)context).dataBase.queryData("UPDATE alarm SET toggle=1 WHERE id ='"+((MainActivity) context).arrayAlarm.get(position).getId()+"' ");
                       } else {
                           alarmClock.setToggle(false);
                           ((MainActivity)context).removeAlarm(((MainActivity) context).arrayAlarm.get(position));//cancel alarm in main activity when press toggle button "OFF"
                           ((MainActivity)context).dataBase.queryData("UPDATE alarm SET toggle=0 WHERE id ='"+((MainActivity) context).arrayAlarm.get(position).getId()+"' ");
                       }
                   }
               }

       );
        return convertView;
    }
}
