package com.joelkreutzwieser.apps.keepass;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class KeePassDrawerLayout extends DrawerLayout {
    public KeePassDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
