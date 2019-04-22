package com.jeasoon.intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseActivity extends Activity {

    private static final String TAG = "zjs";

    @BindView(R.id.FLAG_ACTIVITY_CLEAR_TASK)            CheckBox   FLAG_ACTIVITY_CLEAR_TASK;
    @BindView(R.id.FLAG_ACTIVITY_CLEAR_TOP)             CheckBox   FLAG_ACTIVITY_CLEAR_TOP;
    @BindView(R.id.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)  CheckBox   FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
    @BindView(R.id.FLAG_ACTIVITY_BROUGHT_TO_FRONT)      CheckBox   FLAG_ACTIVITY_BROUGHT_TO_FRONT;
    @BindView(R.id.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) CheckBox   FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;
    @BindView(R.id.FLAG_ACTIVITY_FORWARD_RESULT)        CheckBox   FLAG_ACTIVITY_FORWARD_RESULT;
    @BindView(R.id.FLAG_ACTIVITY_NEW_TASK)              CheckBox   FLAG_ACTIVITY_NEW_TASK;
    @BindView(R.id.FLAG_ACTIVITY_NEW_DOCUMENT)          CheckBox   FLAG_ACTIVITY_NEW_DOCUMENT;
    @BindView(R.id.FLAG_ACTIVITY_MULTIPLE_TASK)         CheckBox   FLAG_ACTIVITY_MULTIPLE_TASK;
    @BindView(R.id.FLAG_ACTIVITY_NO_ANIMATION)          CheckBox   FLAG_ACTIVITY_NO_ANIMATION;
    @BindView(R.id.FLAG_ACTIVITY_NO_HISTORY)            CheckBox   FLAG_ACTIVITY_NO_HISTORY;
    @BindView(R.id.FLAG_ACTIVITY_NO_USER_ACTION)        CheckBox   FLAG_ACTIVITY_NO_USER_ACTION;
    @BindView(R.id.FLAG_ACTIVITY_PREVIOUS_IS_TOP)       CheckBox   FLAG_ACTIVITY_PREVIOUS_IS_TOP;
    @BindView(R.id.FLAG_ACTIVITY_LAUNCH_ADJACENT)       CheckBox   FLAG_ACTIVITY_LAUNCH_ADJACENT;
    @BindView(R.id.FLAG_ACTIVITY_REORDER_TO_FRONT)      CheckBox   FLAG_ACTIVITY_REORDER_TO_FRONT;
    @BindView(R.id.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)  CheckBox   FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
    @BindView(R.id.FLAG_ACTIVITY_RETAIN_IN_RECENTS)     CheckBox   FLAG_ACTIVITY_RETAIN_IN_RECENTS;
    @BindView(R.id.FLAG_ACTIVITY_SINGLE_TOP)            CheckBox   FLAG_ACTIVITY_SINGLE_TOP;
    @BindView(R.id.FLAG_ACTIVITY_TASK_ON_HOME)          CheckBox   FLAG_ACTIVITY_TASK_ON_HOME;
    @BindView(R.id.FLAG_DEBUG_LOG_RESOLUTION)           CheckBox   FLAG_DEBUG_LOG_RESOLUTION;
    @BindView(R.id.FLAG_EXCLUDE_STOPPED_PACKAGES)       CheckBox   FLAG_EXCLUDE_STOPPED_PACKAGES;
    @BindView(R.id.FLAG_FROM_BACKGROUND)                CheckBox   FLAG_FROM_BACKGROUND;
    @BindView(R.id.sv_flags_container)                  ScrollView svFlagsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initCheckBoxByFlags();
        initView();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onCreate: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    protected void initView() {
        svFlagsContainer.post(() -> svFlagsContainer.scrollTo(getIntent().getIntExtra("sv_x", 0), getIntent().getIntExtra("sv_y", 0)));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onNewIntent: activity=%s, hashCode=0x%X, data=%s", getClass().getSimpleName(), hashCode(), String.valueOf(intent)));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onRestart: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onStart: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onResume: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onPause: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onStop: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onDestroy: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    public void finish() {
        super.finish();
        Log.e(TAG, String.format(Locale.ENGLISH, "Action -> finish: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                setAllCheckBoxSelection(true);
                return true;
            case R.id.cancel_select_all:
                setAllCheckBoxSelection(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAllCheckBoxSelection(boolean select) {
        FLAG_ACTIVITY_CLEAR_TASK.setChecked(select);
        FLAG_ACTIVITY_CLEAR_TOP.setChecked(select);
        FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS.setChecked(select);
        FLAG_ACTIVITY_BROUGHT_TO_FRONT.setChecked(select);
        FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY.setChecked(select);
        FLAG_ACTIVITY_FORWARD_RESULT.setChecked(select);
        FLAG_ACTIVITY_NEW_TASK.setChecked(select);
        FLAG_ACTIVITY_NEW_DOCUMENT.setChecked(select);
        FLAG_ACTIVITY_MULTIPLE_TASK.setChecked(select);
        FLAG_ACTIVITY_NO_ANIMATION.setChecked(select);
        FLAG_ACTIVITY_NO_HISTORY.setChecked(select);
        FLAG_ACTIVITY_NO_USER_ACTION.setChecked(select);
        FLAG_ACTIVITY_PREVIOUS_IS_TOP.setChecked(select);
        FLAG_ACTIVITY_LAUNCH_ADJACENT.setChecked(select);
        FLAG_ACTIVITY_REORDER_TO_FRONT.setChecked(select);
        FLAG_ACTIVITY_RESET_TASK_IF_NEEDED.setChecked(select);
        FLAG_ACTIVITY_RETAIN_IN_RECENTS.setChecked(select);
        FLAG_ACTIVITY_SINGLE_TOP.setChecked(select);
        FLAG_ACTIVITY_TASK_ON_HOME.setChecked(select);
        FLAG_DEBUG_LOG_RESOLUTION.setChecked(select);
        FLAG_EXCLUDE_STOPPED_PACKAGES.setChecked(select);
        FLAG_FROM_BACKGROUND.setChecked(select);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, String.format(Locale.ENGLISH, "Action -> onActivityResult: activity=%s, hashCode=0x%X, requestCode=%d, resultCode=%d, data=%s", getClass().getSimpleName(), hashCode(), requestCode, resultCode, String.valueOf(data)));
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.e(TAG, String.format(Locale.ENGLISH, "Action -> onUserLeaveHint: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @OnClick(R.id.standard)
    void standard(View view) {
        startActivity(StandardActivity.class);
    }

    @OnClick(R.id.singleTop)
    void singleTop(View view) {
        startActivity(SingleTopActivity.class);
    }

    @OnClick(R.id.singleTask)
    void singleTask(View view) {
        startActivity(SingleTaskActivity.class);
    }

    @OnClick(R.id.singleInstance)
    void singleInstance(View view) {
        startActivity(SingleInstanceActivity.class);
    }

    @OnClick(R.id.standard_forResult)
    void standardForResult(View view) {
        startActivityForResult(StandardActivity.class);
    }

    @OnClick(R.id.singleTop_forResult)
    void singleTopForResult(View view) {
        startActivityForResult(SingleTopActivity.class);
    }

    @OnClick(R.id.singleTask_forResult)
    void singleTaskForResult(View view) {
        startActivityForResult(SingleTaskActivity.class);
    }

    @OnClick(R.id.singleInstance_forResult)
    void singleInstanceForResult(View view) {
        startActivityForResult(SingleInstanceActivity.class);
    }

    private void startActivity(Class<? extends BaseActivity> target) {
        Intent intent = new Intent(this, target);
        intent.setFlags(collectFlags());
        intent.putExtra("sv_x", svFlagsContainer.getScrollX());
        intent.putExtra("sv_y", svFlagsContainer.getScrollY());
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, String.format(Locale.ENGLISH, "启动失败: %s", e.getMessage()), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void startActivityForResult(Class<? extends BaseActivity> target) {
        Intent intent = new Intent(this, target);
        intent.setFlags(collectFlags());
        try {
            startActivityForResult(intent, hashCode() & 0xFFFF);
        } catch (Exception e) {
            Toast.makeText(this, String.format(Locale.ENGLISH, "启动失败: %s", e.getMessage()), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private int collectFlags() {
        int flag = 0;
        flag = collectFlags(FLAG_ACTIVITY_CLEAR_TASK, flag, Intent.FLAG_ACTIVITY_CLEAR_TASK);
        flag = collectFlags(FLAG_ACTIVITY_CLEAR_TOP, flag, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        flag = collectFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, flag, Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        flag = collectFlags(FLAG_ACTIVITY_BROUGHT_TO_FRONT, flag, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        flag = collectFlags(FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, flag, Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        flag = collectFlags(FLAG_ACTIVITY_FORWARD_RESULT, flag, Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        flag = collectFlags(FLAG_ACTIVITY_NEW_TASK, flag, Intent.FLAG_ACTIVITY_NEW_TASK);
        flag = collectFlags(FLAG_ACTIVITY_NEW_DOCUMENT, flag, Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        flag = collectFlags(FLAG_ACTIVITY_MULTIPLE_TASK, flag, Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        flag = collectFlags(FLAG_ACTIVITY_NO_ANIMATION, flag, Intent.FLAG_ACTIVITY_NO_ANIMATION);
        flag = collectFlags(FLAG_ACTIVITY_NO_HISTORY, flag, Intent.FLAG_ACTIVITY_NO_HISTORY);
        flag = collectFlags(FLAG_ACTIVITY_NO_USER_ACTION, flag, Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        flag = collectFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP, flag, Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        flag = collectFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT, flag, Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        flag = collectFlags(FLAG_ACTIVITY_REORDER_TO_FRONT, flag, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        flag = collectFlags(FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, flag, Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        flag = collectFlags(FLAG_ACTIVITY_RETAIN_IN_RECENTS, flag, Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        flag = collectFlags(FLAG_ACTIVITY_SINGLE_TOP, flag, Intent.FLAG_ACTIVITY_SINGLE_TOP);
        flag = collectFlags(FLAG_ACTIVITY_TASK_ON_HOME, flag, Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        flag = collectFlags(FLAG_DEBUG_LOG_RESOLUTION, flag, Intent.FLAG_DEBUG_LOG_RESOLUTION);
        flag = collectFlags(FLAG_EXCLUDE_STOPPED_PACKAGES, flag, Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        flag = collectFlags(FLAG_FROM_BACKGROUND, flag, Intent.FLAG_FROM_BACKGROUND);
        return flag;
    }

    private int collectFlags(CheckBox CheckBox, int flag, int mask) {
        if (CheckBox.isChecked()) {
            flag |= mask;
        } else {
            flag &= ~mask;
        }
        return flag;
    }

    private void initCheckBoxByFlags() {
        int flags = getIntent().getFlags();
        initCheckBoxByFlags(FLAG_ACTIVITY_CLEAR_TASK, flags, Intent.FLAG_ACTIVITY_CLEAR_TASK);
        initCheckBoxByFlags(FLAG_ACTIVITY_CLEAR_TOP, flags, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        initCheckBoxByFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, flags, Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        initCheckBoxByFlags(FLAG_ACTIVITY_BROUGHT_TO_FRONT, flags, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        initCheckBoxByFlags(FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, flags, Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        initCheckBoxByFlags(FLAG_ACTIVITY_FORWARD_RESULT, flags, Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        initCheckBoxByFlags(FLAG_ACTIVITY_NEW_TASK, flags, Intent.FLAG_ACTIVITY_NEW_TASK);
        initCheckBoxByFlags(FLAG_ACTIVITY_NEW_DOCUMENT, flags, Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        initCheckBoxByFlags(FLAG_ACTIVITY_MULTIPLE_TASK, flags, Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        initCheckBoxByFlags(FLAG_ACTIVITY_NO_ANIMATION, flags, Intent.FLAG_ACTIVITY_NO_ANIMATION);
        initCheckBoxByFlags(FLAG_ACTIVITY_NO_HISTORY, flags, Intent.FLAG_ACTIVITY_NO_HISTORY);
        initCheckBoxByFlags(FLAG_ACTIVITY_NO_USER_ACTION, flags, Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        initCheckBoxByFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP, flags, Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        initCheckBoxByFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT, flags, Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        initCheckBoxByFlags(FLAG_ACTIVITY_REORDER_TO_FRONT, flags, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        initCheckBoxByFlags(FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, flags, Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        initCheckBoxByFlags(FLAG_ACTIVITY_RETAIN_IN_RECENTS, flags, Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        initCheckBoxByFlags(FLAG_ACTIVITY_SINGLE_TOP, flags, Intent.FLAG_ACTIVITY_SINGLE_TOP);
        initCheckBoxByFlags(FLAG_ACTIVITY_TASK_ON_HOME, flags, Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        initCheckBoxByFlags(FLAG_DEBUG_LOG_RESOLUTION, flags, Intent.FLAG_DEBUG_LOG_RESOLUTION);
        initCheckBoxByFlags(FLAG_EXCLUDE_STOPPED_PACKAGES, flags, Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        initCheckBoxByFlags(FLAG_FROM_BACKGROUND, flags, Intent.FLAG_FROM_BACKGROUND);
    }

    private void initCheckBoxByFlags(CheckBox CheckBox, int flag, int mask) {
        CheckBox.setChecked((flag & mask) > 0);
    }

}
