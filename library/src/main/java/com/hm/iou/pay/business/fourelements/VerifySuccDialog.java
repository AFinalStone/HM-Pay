package com.hm.iou.pay.business.fourelements;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.hm.iou.pay.R;

/**
 * Created by hjy on 2018/7/25.
 */

public class VerifySuccDialog extends Dialog implements View.OnClickListener {

    private DialogInterface.OnClickListener mListener;

    public VerifySuccDialog(@NonNull Context context) {
        super(context, R.style.UikitAlertDialogStyle);
        setContentView(R.layout.pay_dialog_four_elements_succ);
        findViewById(R.id.btn_fourelement_confirm).setOnClickListener(this);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_fourelement_confirm) {
            dismiss();
            if (mListener != null) {
                mListener.onClick(this, 0);
            }
        }
    }

    public void setConfirmListener(DialogInterface.OnClickListener listener) {
        mListener = listener;
    }

}
