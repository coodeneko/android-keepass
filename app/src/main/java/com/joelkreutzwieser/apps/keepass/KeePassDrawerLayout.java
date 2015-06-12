package com.joelkreutzwieser.apps.keepass;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class KeePassDrawerLayout extends DrawerLayout {

    public KeePassDrawerLayout(Context context) {
        super(context);
    }

    public KeePassDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeePassDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean isEntryTarget(MotionEvent event) {
        return false;
    }
}
