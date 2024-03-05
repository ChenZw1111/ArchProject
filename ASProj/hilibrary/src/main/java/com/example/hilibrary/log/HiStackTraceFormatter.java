package com.example.hilibrary.log;

public class HiStackTraceFormatter implements HiLogFormatter<StackTraceElement[]>{
    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder builder = new StringBuilder(128);
        if(stackTrace == null || stackTrace.length==0){
            return null;
        }else if(stackTrace.length==1){
            return "\t- "+ stackTrace[0].toString();
        }else{
            int len = stackTrace.length;
            for (int i = 0;i < len; i++) {
                if(i == 0){
                    builder.append("stackTrace: \n");
                }
                if(i !=len -1){
                    builder.append("\t 「");
                    builder.append(stackTrace[i].toString());
                    builder.append("\n");
                }else{
                    builder.append("\t [");
                    builder.append(stackTrace[i].toString());
                }
            }
        }
        return builder.toString();
    }
}
