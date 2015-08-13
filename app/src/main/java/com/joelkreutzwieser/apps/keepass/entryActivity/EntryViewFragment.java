package com.joelkreutzwieser.apps.keepass.entryActivity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.R;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Property;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    Entry activeEntry;
    List<Property> properties;


    public EntryViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_entry_view, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.propertyList);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //properties = activeEntry.getProperties();
        adapter = new EntryViewPropertyAdapter(null);
        recyclerView.setAdapter(adapter);

        return layout;
    }

    public void setActiveEntry(Entry entry) {
        activeEntry = entry;
        properties = activeEntry.getProperties();
        adapter = new EntryViewPropertyAdapter(properties);
        recyclerView.swapAdapter(adapter, false);
    }

    public void clickCopyProperty(View view) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        int selectedPosition = recyclerView.getChildLayoutPosition((View) view.getParent().getParent());
        Property property = properties.get(selectedPosition);
        ClipData clip = ClipData.newPlainText(property.getKey(), property.getValue());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity().getApplicationContext(), property.getKey() + " copied to Clipboard", Toast.LENGTH_SHORT).show();
    }

    public void clickVisibilityProperty(View view) {
        ImageView passwordVisibility = (ImageView) ((View) view.getParent().getParent()).findViewById(R.id.propertyVisibility);
        TextView password = (TextView) ((View) view.getParent().getParent()).findViewById(R.id.propertyEntry);
        Typeface typeface = password.getTypeface();
        if (password.getTransformationMethod() == null) {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisibility.setImageResource(R.drawable.ic_eye_black_24dp);
        } else {
            password.setTransformationMethod(null);
            passwordVisibility.setImageResource(R.drawable.ic_eye_off_black_24dp);
        }
        password.setTypeface(typeface);
    }

    public String getUser() {
        return activeEntry.getUserName();
    }

    public String getPassword() {
        return activeEntry.getPassword();
    }
}
