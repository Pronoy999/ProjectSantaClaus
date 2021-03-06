package com.pm.wd.sl.college.projectsantaclaus.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm.wd.sl.college.projectsantaclaus.Activities.NewMessageActivity;
import com.pm.wd.sl.college.projectsantaclaus.Objects.Message;
import com.pm.wd.sl.college.projectsantaclaus.Objects.MsgApp;
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
            vh._attachImage = convertView.findViewById(R.id.attachImage);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (msg != null) {
            vh._msgTextView.setText(msg.getMsg());
            vh._attachImage.setVisibility(msg.getUrl().isEmpty() ? View.GONE : View.VISIBLE);
            boolean finalSend_msg = send_msg;
            vh._attachImage.setOnClickListener(v -> {
                Intent i = new Intent(getContext(), NewMessageActivity.class)
                        .putExtra("is_view_msg", true)
                        .putExtra("other_person_id", finalSend_msg ? msg.getRecvrUid() : msg.getSendrUid())
                        .putExtra("param_message", msg);
                getContext().startActivity(i);
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView _msgTextView;
        ImageView _attachImage;
    }
}
