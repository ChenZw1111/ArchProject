package com.example.hilibrary.log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HiLogMo {
    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yy-MM-dd HH:mm:ss", Locale.CHINA
    );
    public long timeMillis;
    public int level;
    public String tag;
    public String log;

    public HiLogMo(long timeMillis, int level, String tag, String log) {
        this.timeMillis = timeMillis;
        this.log = log;
        this.tag = tag;
        this.level = level;
    }

    public String flattenedLog() {
        return getFlattened() + "\n" + log;
    }

    public String getFlattened() {
        return format(timeMillis) + '|' + level + "|" + tag + "|";
    }

    public String format(long timeMillis) {
        return sdf.format(timeMillis);
    }
}
