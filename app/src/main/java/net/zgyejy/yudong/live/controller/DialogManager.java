package net.zgyejy.yudong.live.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;

/**
 * 管理Dialog的类
 * Created by Administrator on 2017/1/19 0019.
 */

public class DialogManager {
    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;

    private Context mContext;

    private AlertDialog dialog;//用于取消AlertDialog.Builder

    /**
     * 构造方法 传入上下文
     */
    public DialogManager(Context context) {
        this.mContext = context;
    }

    // 显示录音的对话框
    public void showRecordingDialog() {

        mDialog = new Dialog(mContext,R.style.Theme_audioDialog);
        // 用layoutinflater来引用布局
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_manager, null);
        mDialog.setContentView(view);


        mIcon = (ImageView) mDialog.findViewById(R.id.dialog_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.dialog_voice);
        mLable = (TextView) mDialog.findViewById(R.id.recorder_dialogtext);
        mDialog.show();
    }

    public void recording(){
        if(dialog != null && dialog.isShowing()){ //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("手指上滑，取消发送");
        }
    }

    // 显示想取消的对话框
    public void wantToCancel() {
        if(dialog != null && dialog.isShowing()){ //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("松开手指，取消发送");
        }
    }

    // 显示时间过短的对话框
    public void tooShort() {
        if(dialog != null && dialog.isShowing()){ //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("录音时间过短");
        }
    }

    // 显示取消的对话框
    public void dimissDialog() {
        if(dialog != null && dialog.isShowing()){ //显示状态
            dialog.dismiss();
            dialog = null;
        }
    }

    // 显示更新音量级别的对话框
    public void updateVoiceLevel(int level) {
        if(dialog != null && dialog.isShowing()){ //显示状态
//          mIcon.setVisibility(View.VISIBLE);
//          mVoice.setVisibility(View.VISIBLE);
//          mLable.setVisibility(View.VISIBLE);

            //设置图片的id
            int resId = mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }

}
