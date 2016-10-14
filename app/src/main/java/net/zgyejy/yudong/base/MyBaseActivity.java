package net.zgyejy.yudong.base;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import net.zgyejy.yudong.R;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyBaseActivity extends FragmentActivity {
    private static final String TAG = "MyBaseActivity";
    private Toast toast;
    //screenW，screenH分别代表当前手机屏幕的宽，高
    public static int screenW, screenH;
    public Dialog dialog;//界面弹出框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenW = getWindowManager().getDefaultDisplay().getWidth();
        screenH = getWindowManager().getDefaultDisplay().getHeight();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void showToast(String msg) {
        if (toast == null)
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, null);
    }

    public void openActivity(Class<?> pClass, Bundle bundle) {
        openActivity(pClass, bundle, null);
    }

    public void openActivity(Class<?> pClass, Bundle bundle, Uri uri) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null)
            intent.putExtras(bundle);
        if (uri != null)
            intent.setData(uri);
        startActivity(intent);
        //增加动画
        overridePendingTransition(R.anim.anim_activity_right_in, R.anim.anim_activity_bottom_out);
    }

//    /**
//     * 显示加载动画
//     *
//     * @param context 上下文
//     * @param msg     TextView显示的文本内容
//     * @param cancle  控制是否点击隐藏
//     *//*
//    public void showLoadingDialong(Context context, String msg, boolean cancle) {
//        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
//        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_loading);
//        ImageView iv = (ImageView) view.findViewById(R.id.iv_loading_img);
//        TextView tv = (TextView) view.findViewById(R.id.tv_loading_msg);
//
//        Animation roatAnim = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
//        iv.setAnimation(roatAnim);
//        if (null != msg) {
//            tv.setText(msg);
//        }
//        dialog = new Dialog(context, R.style.loading_dialog);
//        dialog.setContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams
//                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        dialog.setCancelable(cancle);
//        dialog.show();
//    }*/

    public void cancleDialong() {
        if (null!=dialog)
            dialog.dismiss();
    }
}
