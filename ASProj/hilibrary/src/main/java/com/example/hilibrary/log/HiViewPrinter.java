package com.example.hilibrary.log;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hilibrary.R;

import java.util.ArrayList;
import java.util.List;

public class HiViewPrinter implements HiLogPrinter {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private HiViewPrinterProvider viewProvider;
    public HiViewPrinter(Activity activity){
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        recyclerView = new RecyclerView(activity);
        adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewProvider = new HiViewPrinterProvider(rootView,recyclerView);
    }


    public HiViewPrinterProvider getViewProvider(){
        return viewProvider;
    }
    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        adapter.addItem(new HiLogMo(System.currentTimeMillis(),level,tag,printString));
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
        private LayoutInflater inflater;
        private List<HiLogMo> logs=new ArrayList<>();

        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(HiLogMo logItem) {
            logs.add(logItem);
            notifyItemInserted(logs.size() - 1);
        }

        /**
         * 根据日志级别获取高亮颜色
         *
         * @param logLevel
         * @return
         */
        private int getHighLightColor(int logLevel) {
            int highlight;
            switch (logLevel) {
                case HiLogType.V:
                    highlight = 0xffbbbbbb;
                    break;
                case HiLogType.E:
                    highlight = 0xffff6b68;
                    break;
                case HiLogType.D:
                    highlight = 0xffffffff;
                    break;
                case HiLogType.W:
                    highlight = 0xffbbb529;
                    break;
                case HiLogType.I:
                    highlight = 0xff6a8759;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;
            }
            return highlight;
        }

        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.hilog_item, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            HiLogMo logItem =logs.get(position);
            int color = getHighLightColor(logItem.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);

            holder.tagView.setText(logItem.flattenedLog());
            holder.messageView.setText(logItem.log);
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tagView;
        TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
