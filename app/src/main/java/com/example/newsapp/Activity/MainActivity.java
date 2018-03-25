package com.example.newsapp.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newsapp.Fragments.ContactsFragment;
import com.example.newsapp.Fragments.MessagesFragment;
import com.example.newsapp.Fragments.NewsFragment;
import com.example.newsapp.Fragments.SettingsFragment;
import com.example.newsapp.R;

public class MainActivity extends Activity implements View.OnClickListener{

    private LinearLayout fag1;
    private LinearLayout fag2;
    private LinearLayout fag3;
    private LinearLayout fag4;

    private TextView tx1;
    private TextView tx2;
    private TextView tx3;
    private TextView tx4;

    private TextPaint tp1;
    private TextPaint tp2;
    private TextPaint tp3;
    private TextPaint tp4;

    private ContactsFragment contactsFragment;
    private NewsFragment newsFragment;
    private MessagesFragment messagesFragment;
    private SettingsFragment settingsFragment;

    private FragmentManager fragmentManager;

    private Drawable drawable11 = null;
    private Drawable drawable12 = null;
    private Drawable drawable21 = null;
    private Drawable drawable22 = null;
    private Drawable drawable31 = null;
    private Drawable drawable32 = null;
    private Drawable drawable41 = null;
    private Drawable drawable42 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initdrawable();
        initviews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    private void initdrawable() {
        drawable11 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.home_black);
        drawable11.setBounds(0,0,50,50);
        drawable12 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.home_blue);
        drawable12.setBounds(0,0,50,50);
        drawable21 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.list_black);
        drawable21.setBounds(0,0,50,50);
        drawable22 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.list_blue);
        drawable22.setBounds(0,0,50,50);
        drawable31 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.msg_black);
        drawable31.setBounds(0,0,50,50);
        drawable32 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.msg_blue);
        drawable32.setBounds(0,0,50,50);
        drawable41 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.character_black);
        drawable41.setBounds(0,0,50,50);
        drawable42 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.character_blue);
        drawable42.setBounds(0,0,50,50);
    }

    private void setdrawable(TextView tx,Drawable dw){
        tx.setCompoundDrawables(null,dw,null,null);
        tx.setCompoundDrawablePadding(5);
    }

    private void initviews() {
        fag1 = findViewById(R.id.fra1);
        fag2 = findViewById(R.id.fra2);
        fag3 = findViewById(R.id.fra3);
        fag4 = findViewById(R.id.fra4);
        tx1 = findViewById(R.id.messagestext);
        tp1 = tx1.getPaint();
        tx2 = findViewById(R.id.contactstext);
        tp2 = tx2.getPaint();
        tx3 = findViewById(R.id.newstext);
        tp3 = tx3.getPaint();
        tx4 = findViewById(R.id.settingtext);
        tp4 = tx4.getPaint();
        setdrawable(tx1,drawable11);
        setdrawable(tx2,drawable21);
        setdrawable(tx3,drawable31);
        setdrawable(tx4,drawable41);
        fag1.setOnClickListener(this);
        fag2.setOnClickListener(this);
        fag3.setOnClickListener(this);
        fag4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fra1:
                setTabSelection(0);
                break;
            case R.id.fra2:
                setTabSelection(1);
                break;
            case R.id.fra3:
                setTabSelection(2);
                break;
            case R.id.fra4:
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int i) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        clearFakeBoldText();
        switch (i){
            case 0:
                setdrawable(tx1,drawable12);
                if(messagesFragment == null){
                    messagesFragment = new MessagesFragment();
                    transaction.add(R.id.content,messagesFragment);
                }
                transaction.show(messagesFragment);
                tx1.setTextColor(0xFF63B8FF);
                tp1.setFakeBoldText(true);
                break;
            case 1:
                setdrawable(tx2,drawable22);
                if(contactsFragment == null){
                    contactsFragment = new ContactsFragment();
                    transaction.add(R.id.content,contactsFragment);
                }
                transaction.show(contactsFragment);
                tx2.setTextColor(0xFF63B8FF);
                tp2.setFakeBoldText(true);
                break;
            case 2:
                setdrawable(tx3,drawable32);
                if(newsFragment == null){
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.content,newsFragment);
                }
                transaction.show(newsFragment);
                tx3.setTextColor(0xFF63B8FF);
                tp3.setFakeBoldText(true);
                break;
            case 3:
                setdrawable(tx4,drawable42);
                if(settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    transaction.add(R.id.content, settingsFragment);
                }
                transaction.show(settingsFragment);
                tx4.setTextColor(0xFF63B8FF);
                tp4.setFakeBoldText(true);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void clearFakeBoldText() {
        tx1.setTextColor(Color.BLACK);
        tx2.setTextColor(Color.BLACK);
        tx3.setTextColor(Color.BLACK);
        tx4.setTextColor(Color.BLACK);
        tp1.setFakeBoldText(false);
        tp2.setFakeBoldText(false);
        tp3.setFakeBoldText(false);
        tp4.setFakeBoldText(false);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (messagesFragment != null) {
            transaction.hide(messagesFragment);
        }
        if (contactsFragment != null) {
            transaction.hide(contactsFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }
    }

    private void clearSelection() {
        setdrawable(tx1,drawable11);
        setdrawable(tx2,drawable21);
        setdrawable(tx3,drawable31);
        setdrawable(tx4,drawable41);
    }
}
