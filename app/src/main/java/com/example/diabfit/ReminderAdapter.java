package com.example.diabfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList;
    private final LayoutInflater inflater;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault());


    public interface OnReminderListener {
        void onEditClick(Reminder reminder);
        void onDeleteClick(Reminder reminder);
    }

    private OnReminderListener listener;

    public ReminderAdapter(Context context, List<Reminder> reminderList, OnReminderListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.reminderList = reminderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.reminder_item, parent, false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder current = reminderList.get(position);

        holder.textViewDescription.setText(current.getDescription());

        holder.textViewDateTime.setText(dateTimeFormat.format(new Date(current.getReminderTime())));

        holder.buttonEdit.setOnClickListener(v -> listener.onEditClick(current));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClick(current));
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }


    public void setReminders(List<Reminder> reminders) {
        this.reminderList = reminders;
        notifyDataSetChanged();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDescription;
        private final TextView textViewDateTime;
        private final ImageButton buttonEdit;
        private final ImageButton buttonDelete;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
