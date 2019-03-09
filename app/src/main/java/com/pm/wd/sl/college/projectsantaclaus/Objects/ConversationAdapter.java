package com.pm.wd.sl.college.projectsantaclaus.Objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pm.wd.sl.college.projectsantaclaus.R;

import java.util.List;

public class ConversationAdapter extends ArrayAdapter<Message> {
    public ConversationAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        boolean send_msg = true;
        Message msg = getItem(position);
        if (msg != null) {
            send_msg = msg.getSendrUid().equals(MsgApp.instance().user.getEmail());
        }

        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(send_msg ? R.layout.list_item_converse_message_send : R.layout.list_item_converse_message_recv, parent, false);
            vh = new ViewHolder();
            vh._msgTextView = convertView.findViewById(R.id.msgTextView);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (msg != null) {
            vh._msgTextView.setText(msg.getMsg());
        }
        return convertView;
    }

    class ViewHolder {
        TextView _msgTextView;
    }
}
