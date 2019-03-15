package com.pm.wd.sl.college.projectsantaclaus.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewH;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);

            viewH = new ViewHolder();
            viewH._fname = convertView.findViewById(R.id.msgFNameView);
            viewH._lname = convertView.findViewById(R.id.msgLNameView);
            viewH._date = convertView.findViewById(R.id.msgDateView);
            viewH._time = convertView.findViewById(R.id.msgTimeView);
            viewH._msg = convertView.findViewById(R.id.msgMsgView);
            viewH._img = convertView.findViewById(R.id.msgImageView);

            convertView.setTag(viewH);
        } else {
            viewH = (ViewHolder) convertView.getTag();
        }

        final Message msg = getItem(position);

        if (msg != null) {
            viewH._fname.setText(msg.getSender().getFirstName());
            viewH._lname.setText(msg.getSender().getLastName());
            viewH._date.setText(msg.getDate());
            viewH._time.setText(msg.getTime());
            viewH._msg.setText(msg.getMsg());
            Glide.with(getContext())
                    .load(msg.getUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(viewH._img);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView _fname;
        TextView _lname;
        TextView _date;
        TextView _time;
        TextView _msg;
        ImageView _img;
    }
}
