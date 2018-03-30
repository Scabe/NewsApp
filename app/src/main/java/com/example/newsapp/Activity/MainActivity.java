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

    private LinearLayout ll_messages;
    private LinearLayout ll_contacts;
    private LinearLayout ll_news;
    private LinearLayout ll_settings;

    private TextView tx_messages;
    private TextView tx_contacts;
    private TextView tx_news;
    private TextView tx_settings;

    private TextPaint tp_messages;
    private TextPaint tp_contacts;
    private TextPaint tp_news;
    private TextPaint tp_settings;

    private ContactsFragment contactsFragment;
    private NewsFragment newsFragment;
    private MessagesFragment messagesFragment;
    private SettingsFragment settingsFragment;

    private FragmentManager fragmentManager;

    private Drawable drawable_messages_normal = null;
    private Drawable drawable_messages_click = null;
    private Drawable drawable_contacts_normal = null;
    private Drawable drawable_contacts_click = null;
    private Drawable drawable_news_normal = null;
    private Drawable drawable_news_click = null;
    private Drawable drawable_settings_normal = null;
    private Drawable drawable_settings_click = null;


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
        drawable_messages_normal = ContextCompat.getDrawable(getApplicationContext(),R.drawable.home_black);
        drawable_messages_normal.setBounds(0,0,50,50);
        drawable_messages_click = ContextCompat.getDrawable(getApplicationContext(),R.drawable.home_blue);
        drawable_messages_click.setBounds(0,0,50,50);
        drawable_contacts_normal = ContextCompat.getDrawable(getApplicationContext(),R.drawable.list_black);
        drawable_contacts_normal.setBounds(0,0,50,50);
        drawable_contacts_click = ContextCompat.getDrawable(getApplicationContext(),R.drawable.list_blue);
        drawable_contacts_click.setBounds(0,0,50,50);
        drawable_news_normal = ContextCompat.getDrawable(getApplicationContext(),R.drawable.msg_black);
        drawable_news_normal.setBounds(0,0,50,50);
        drawable_news_click = ContextCompat.getDrawable(getApplicationContext(),R.drawable.msg_blue);
        drawable_news_click.setBounds(0,0,50,50);
        drawable_settings_normal = ContextCompat.getDrawable(getApplicationContext(),R.drawable.character_black);
        drawable_settings_normal.setBounds(0,0,50,50);
        drawable_settings_click = ContextCompat.getDrawable(getApplicationContext(),R.drawable.character_blue);
        drawable_settings_click.setBounds(0,0,50,50);
    }

    private void setdrawable(TextView tx,Drawable dw){
        tx.setCompoundDrawables(null,dw,null,null);
        tx.setCompoundDrawablePadding(5);
    }

    private void initviews() {
        ll_messages = findViewById(R.id.fra1);
        ll_contacts = findViewById(R.id.fra2);
        ll_news = findViewById(R.id.fra3);
        ll_settings = findViewById(R.id.fra4);
        tx_messages = findViewById(R.id.messagestext);
        tp_messages = tx_messages.getPaint();
        tx_contacts = findViewById(R.id.contactstext);
        tp_contacts = tx_contacts.getPaint();
        tx_news = findViewById(R.id.newstext);
        tp_news = tx_news.getPaint();
        tx_settings = findViewById(R.id.settingtext);
        tp_settings = tx_settings.getPaint();
        setdrawable(tx_messages,drawable_messages_normal);
        setdrawable(tx_contacts,drawable_contacts_normal);
        setdrawable(tx_news,drawable_news_normal);
        setdrawable(tx_settings,drawable_settings_normal);
        ll_messages.setOnClickListener(this);
        ll_contacts.setOnClickListener(this);
        ll_news.setOnClickListener(this);
        ll_settings.setOnClickListener(this);
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
                setdrawable(tx_messages,drawable_messages_click);
                if(messagesFragment == null){
                    messagesFragment = new MessagesFragment();
                    transaction.add(R.id.content,messagesFragment);
                }
                transaction.show(messagesFragment);
                tx_messages.setTextColor(0xFF63B8FF);
                tp_messages.setFakeBoldText(true);
                break;
            case 1:
                setdrawable(tx_contacts,drawable_contacts_click);
                if(contactsFragment == null){
                    contactsFragment = new ContactsFragment();
                    transaction.add(R.id.content,contactsFragment);
                }
                transaction.show(contactsFragment);
                tx_contacts.setTextColor(0xFF63B8FF);
                tp_contacts.setFakeBoldText(true);
                break;
            case 2:
                setdrawable(tx_news,drawable_news_click);
                if(newsFragment == null){
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.content,newsFragment);
                }
                transaction.show(newsFragment);
                tx_news.setTextColor(0xFF63B8FF);
                tp_news.setFakeBoldText(true);
                break;
            case 3:
                setdrawable(tx_settings,drawable_settings_click);
                if(settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    transaction.add(R.id.content, settingsFragment);
                }
                transaction.show(settingsFragment);
                tx_settings.setTextColor(0xFF63B8FF);
                tp_settings.setFakeBoldText(true);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void clearFakeBoldText() {
        tx_messages.setTextColor(Color.BLACK);
        tx_contacts.setTextColor(Color.BLACK);
        tx_news.setTextColor(Color.BLACK);
        tx_settings.setTextColor(Color.BLACK);
        tp_messages.setFakeBoldText(false);
        tp_contacts.setFakeBoldText(false);
        tp_news.setFakeBoldText(false);
        tp_settings.setFakeBoldText(false);
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
        setdrawable(tx_messages,drawable_messages_normal);
        setdrawable(tx_contacts,drawable_contacts_normal);
        setdrawable(tx_news,drawable_news_normal);
        setdrawable(tx_settings,drawable_settings_normal);
    }
}
