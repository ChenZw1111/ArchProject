package com.example.asproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.asproj.logic.MainActivityLogic;
import com.example.asproj.restful.HiCallback;
import com.example.asproj.restful.HiResponse;
import com.example.asproj.restful.http.ApiFactory;
import com.example.common.HiBaseActivity;
import com.example.hilibrary.log.HiLog;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {
    private static String TAG = "MainActivity";
    private MainActivityLogic activityLogic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityLogic = new MainActivityLogic(this,savedInstanceState);
        startActivity(new Intent(this,
                LoginActivity.class));

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        activityLogic.onSaveInstanceState(outState);
        Log.e("currentItemIndex", "111111");
    }

    /**
     * 利用系统CallLog获取通话历史记录
     * @param activity
     * @param num  要读取记录的数量
     * @return
     */
    public void getCallHistoryList(Activity activity, int num) {
        // 检查是否已经获取了所需的权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            // 已经获取了权限，可以读取来电记录
            readCallLog();
        } else {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS}, 1000);
        }
    }

    private void readCallLog() {
        Cursor cs = getContentResolver().query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION,   //通话时长
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = simpleDateFormat.format(date);
            for (cs.moveToFirst(); (!cs.isAfterLast()) && i < 99999; cs.moveToNext(), i++) {
                String callName = cs.getString(0);  //名称
                String callNumber = cs.getString(1);  //号码
                //如果名字为空，在通讯录查询一次有没有对应联系人
//                if (callName == null || callName.equals("")){
//                    String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
//                    //设置查询条件
//                    String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='"+callNumber+"'";
//                    Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            cols, selection, null, null);
//                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//                    if (cursor.getCount()>0){
//                        cursor.moveToFirst();
//                        callName = cursor.getString(nameFieldColumnIndex);
//                    }
//                    cursor.close();
//                }
                //通话类型
                int callType = Integer.parseInt(cs.getString(2));
                String callTypeStr = "";
//                switch (callType) {
//                    case CallLog.Calls.INCOMING_TYPE:
//                        callTypeStr = "CallLogInfo.CALLIN";
//                        break;
//                    case CallLog.Calls.OUTGOING_TYPE:
//                        callTypeStr = "CallLogInfo.CALLOUT";
//                        break;
//                    case CallLog.Calls.MISSED_TYPE:
//                        callTypeStr = "CallLogInfo.CAllMISS";
//                        break;
//                    case CallLog.Calls.BLOCKED_TYPE:
//                        callTypeStr ="BLOCKED_TYPE";
//                    default:
//                        //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
//                        Log.i("ssss", "" + callType);
//                        i--;
//                        continue;
//                }
//                拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                String callDateStr = sdf.format(callDate);
                if (callDateStr.equals(date_today)) { //判断是否为今天
                    sdf = new SimpleDateFormat("HH:mm");
                    callDateStr = sdf.format(callDate);
                } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
                    sdf = new SimpleDateFormat("dd");
                    int callDay = Integer.valueOf(sdf.format(callDate));

                    int day = Integer.valueOf(sdf.format(date));
                    if (day - callDay == 1) {
                        callDateStr = "昨天";
                    } else {
                        sdf = new SimpleDateFormat("MM-dd");
                        callDateStr = sdf.format(callDate);
                    }
                } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
                    sdf = new SimpleDateFormat("MM-dd");
                    callDateStr = sdf.format(callDate);
                }

                //通话时长
                int callDuration = Integer.parseInt(cs.getString(4));
//                int min = callDuration / 60;
//                int sec = callDuration % 60;
                String callDurationStr = callDuration+" 秒";
//                if (sec > 0) {
//                    if (min > 0) {
//                        callDurationStr = min + "分" + sec + "秒";
//                    } else {
//                        callDurationStr = sec + "秒";
//                    }
//                }

                /**
                 * callName 名字
                 * callNumber 号码
                 * callTypeStr 通话类型
                 * callDateStr 通话日期
                 * callDurationStr 通话时长
                 * 请在此处执行相关UI或存储操作，之后会查询下一条通话记录
                 */
                if(callType == CallLog.Calls.INCOMING_TYPE && callDuration==0){
                Log.i("Msg","callName："+callName+" callNumber："+callNumber+" callTypeStr："+"CALLIN"+" callDateStr："+callDateStr+" callDurationStr："+callDurationStr);
                }
//                Log.i("Msg","callName："+callName+" callNumber："+callNumber+" callTypeStr："+callTypeStr+" callDateStr："+callDateStr+" callDurationStr："+callDurationStr);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，可以读取来电记录
                readCallLog();
            } else {
                // 用户拒绝了权限请求，您可以根据需要处理
                Toast.makeText(this, "未授予权限，无法读取来电记录", Toast.LENGTH_SHORT).show();
            }
        }
    }
}