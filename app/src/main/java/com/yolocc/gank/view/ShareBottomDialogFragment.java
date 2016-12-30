package com.yolocc.gank.view;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yolocc.gank.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareBottomDialogFragment extends DialogFragment implements View.OnClickListener{

    public static final int WECHAT_FRIEND_TARGET = 1;
    public static final int WECHAT_MOMENT_TARGET = 2;
    public static final int QQ_FRIEND_TARGET = 3;
    public static final int QQ_ZONE_TARGET = 4;

    private OnShareClickListener mOnShareClickListener;

    public ShareBottomDialogFragment() {
        // Required empty public constructor
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        mOnShareClickListener = onShareClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_share_choose);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView mWechatFriendTv = (TextView) dialog.findViewById(R.id.wechat_friend_tv);
        TextView mWechatMomentTv = (TextView) dialog.findViewById(R.id.wechat_moment_tv);
        TextView mQqTv = (TextView) dialog.findViewById(R.id.qq_friend_tv);
        TextView mZoneTv = (TextView) dialog.findViewById(R.id.qq_zone_tv);
        mWechatFriendTv.setOnClickListener(this);
        mWechatMomentTv.setOnClickListener(this);
        mQqTv.setOnClickListener(this);
        mZoneTv.setOnClickListener(this);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_friend_tv:
                mOnShareClickListener.onTargetChose(WECHAT_FRIEND_TARGET);
                break;
            case R.id.wechat_moment_tv:
                mOnShareClickListener.onTargetChose(WECHAT_MOMENT_TARGET);
                break;
            case R.id.qq_friend_tv:
                mOnShareClickListener.onTargetChose(QQ_FRIEND_TARGET);
                break;
            case R.id.qq_zone_tv:
                mOnShareClickListener.onTargetChose(QQ_ZONE_TARGET);
                break;
        }
        getDialog().dismiss();
    }

    public interface OnShareClickListener {
        void onTargetChose(int target);
    }


}