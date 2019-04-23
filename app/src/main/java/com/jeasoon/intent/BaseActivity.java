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

    private static final int TASK_AFFINITY_NONE = 0;
    private static final int TASK_AFFINITY_A = 1;
    private static final int TASK_AFFINITY_B = 2;
    private static final int TASK_AFFINITY_C = 3;

    @BindView(R.id.FLAG_ACTIVITY_CLEAR_TASK)
    CheckBox FLAG_ACTIVITY_CLEAR_TASK;
    @BindView(R.id.FLAG_ACTIVITY_CLEAR_TOP)
    CheckBox FLAG_ACTIVITY_CLEAR_TOP;
    @BindView(R.id.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    CheckBox FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
    @BindView(R.id.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
    CheckBox FLAG_ACTIVITY_BROUGHT_TO_FRONT;
    @BindView(R.id.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
    CheckBox FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;
    @BindView(R.id.FLAG_ACTIVITY_FORWARD_RESULT)
    CheckBox FLAG_ACTIVITY_FORWARD_RESULT;
    @BindView(R.id.FLAG_ACTIVITY_NEW_TASK)
    CheckBox FLAG_ACTIVITY_NEW_TASK;
    @BindView(R.id.FLAG_ACTIVITY_NEW_DOCUMENT)
    CheckBox FLAG_ACTIVITY_NEW_DOCUMENT;
    @BindView(R.id.FLAG_ACTIVITY_MULTIPLE_TASK)
    CheckBox FLAG_ACTIVITY_MULTIPLE_TASK;
    @BindView(R.id.FLAG_ACTIVITY_NO_ANIMATION)
    CheckBox FLAG_ACTIVITY_NO_ANIMATION;
    @BindView(R.id.FLAG_ACTIVITY_NO_HISTORY)
    CheckBox FLAG_ACTIVITY_NO_HISTORY;
    @BindView(R.id.FLAG_ACTIVITY_NO_USER_ACTION)
    CheckBox FLAG_ACTIVITY_NO_USER_ACTION;
    @BindView(R.id.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
    CheckBox FLAG_ACTIVITY_PREVIOUS_IS_TOP;
    @BindView(R.id.FLAG_ACTIVITY_LAUNCH_ADJACENT)
    CheckBox FLAG_ACTIVITY_LAUNCH_ADJACENT;
    @BindView(R.id.FLAG_ACTIVITY_REORDER_TO_FRONT)
    CheckBox FLAG_ACTIVITY_REORDER_TO_FRONT;
    @BindView(R.id.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    CheckBox FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
    @BindView(R.id.FLAG_ACTIVITY_RETAIN_IN_RECENTS)
    CheckBox FLAG_ACTIVITY_RETAIN_IN_RECENTS;
    @BindView(R.id.FLAG_ACTIVITY_SINGLE_TOP)
    CheckBox FLAG_ACTIVITY_SINGLE_TOP;
    @BindView(R.id.FLAG_ACTIVITY_TASK_ON_HOME)
    CheckBox FLAG_ACTIVITY_TASK_ON_HOME;
    @BindView(R.id.FLAG_DEBUG_LOG_RESOLUTION)
    CheckBox FLAG_DEBUG_LOG_RESOLUTION;
    @BindView(R.id.FLAG_EXCLUDE_STOPPED_PACKAGES)
    CheckBox FLAG_EXCLUDE_STOPPED_PACKAGES;
    @BindView(R.id.FLAG_FROM_BACKGROUND)
    CheckBox FLAG_FROM_BACKGROUND;
    @BindView(R.id.sv_flags_container)
    ScrollView svFlagsContainer;

    private int mCurrentAffinity;
    private int mTargetAffinity = TASK_AFFINITY_NONE;
    private String mOriginTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initView();
        initCheckBoxByFlags();
        Log.e(TAG, String.format(Locale.ENGLISH, "Lifecycle -> onCreate: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    protected void initView() {
        svFlagsContainer.post(() -> svFlagsContainer.scrollTo(getIntent().getIntExtra("sv_x", 0), getIntent().getIntExtra("sv_y", 0)));
        CharSequence title = getTitle();
        mOriginTitle = title == null ? " " : title.toString();
        mCurrentAffinity = getIntent().getIntExtra("affinity_cur", TASK_AFFINITY_NONE);
        changeTaskAffinity(mCurrentAffinity);
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
        setResult(hashCode() & 0xFFFF);
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
            case R.id.affinity_none:
                changeTaskAffinity(TASK_AFFINITY_NONE);
                return true;
            case R.id.affinity_a:
                changeTaskAffinity(TASK_AFFINITY_A);
                return true;
            case R.id.affinity_b:
                changeTaskAffinity(TASK_AFFINITY_B);
                return true;
            case R.id.affinity_c:
                changeTaskAffinity(TASK_AFFINITY_C);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTaskAffinity(int target) {
        mTargetAffinity = target;
        String currentAffinity = parseTaskAffinityName(mCurrentAffinity);
        String targetAffinity = parseTaskAffinityName(target);
        setTitle(String.format(Locale.ENGLISH, "%s %s - %s", mOriginTitle, currentAffinity, targetAffinity));
    }

    private String parseTaskAffinityName(int affinity) {
        String targetAffinity;
        switch (affinity) {
            case TASK_AFFINITY_A:
                targetAffinity = "A";
                break;
            case TASK_AFFINITY_B:
                targetAffinity = "B";
                break;
            case TASK_AFFINITY_C:
                targetAffinity = "C";
                break;
            case TASK_AFFINITY_NONE:
            default:
                targetAffinity = "·";
                break;
        }
        return targetAffinity;
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
        Log.e(TAG, String.format(Locale.ENGLISH, "Action -> onActivityResult: activity=%s, hashCode=0x%X, requestCode=0x%X, resultCode=0x%X, data=%s", getClass().getSimpleName(), hashCode(), requestCode, resultCode, String.valueOf(data)));
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.e(TAG, String.format(Locale.ENGLISH, "Action -> onUserLeaveHint: activity=%s, hashCode=0x%X", getClass().getSimpleName(), hashCode()));
    }

    @OnClick(R.id.standard_1)
    void standard_1(View view) {
        startActivity(getStandardActivityClassByTaskAffinity(1));
    }

    @OnClick(R.id.standard_2)
    void standard_2(View view) {
        startActivity(getStandardActivityClassByTaskAffinity(2));
    }

    @OnClick(R.id.standard_3)
    void standard_3(View view) {
        startActivity(getStandardActivityClassByTaskAffinity(3));
    }

    @OnClick(R.id.singleTop)
    void singleTop(View view) {
        startActivity(getSingleTopActivityClassByTaskAffinity());
    }

    @OnClick(R.id.singleTask)
    void singleTask(View view) {
        startActivity(getSingleTaskActivityClassByTaskAffinity());
    }

    @OnClick(R.id.singleInstance)
    void singleInstance(View view) {
        startActivity(getSingleInstanceActivityClassByTaskAffinity());
    }

    @OnClick(R.id.standard_forResult)
    void standardForResult(View view) {
        startActivityForResult(getStandardActivityClassByTaskAffinity(0));
    }

    @OnClick(R.id.singleTop_forResult)
    void singleTopForResult(View view) {
        startActivityForResult(getSingleTopActivityClassByTaskAffinity());
    }

    @OnClick(R.id.singleTask_forResult)
    void singleTaskForResult(View view) {
        startActivityForResult(getSingleTaskActivityClassByTaskAffinity());
    }

    @OnClick(R.id.singleInstance_forResult)
    void singleInstanceForResult(View view) {
        startActivityForResult(getSingleInstanceActivityClassByTaskAffinity());
    }

    @SuppressWarnings("all")
    private Class<? extends BaseActivity> getStandardActivityClassByTaskAffinity(int which) {
        String targetAffinity = "";
        switch (mTargetAffinity) {
            case TASK_AFFINITY_A:
                targetAffinity = "_AffinityA";
                break;
            case TASK_AFFINITY_B:
                targetAffinity = "_AffinityB";
                break;
            case TASK_AFFINITY_C:
                targetAffinity = "_AffinityC";
                break;
            case TASK_AFFINITY_NONE:
            default:
                break;
        }
        if (which <= 0 || which > 3) {
            which = 1;
        }
        try {
            return (Class<? extends BaseActivity>) Class.forName(String.format(Locale.ENGLISH, "%s.StandardActivity%s_%d", getClass().getPackage().getName(), targetAffinity, which));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<? extends BaseActivity> getSingleTopActivityClassByTaskAffinity() {
        switch (mTargetAffinity) {
            case TASK_AFFINITY_A:
                return SingleTopActivity_AffinityA.class;
            case TASK_AFFINITY_B:
                return SingleTopActivity_AffinityB.class;
            case TASK_AFFINITY_C:
                return SingleTopActivity_AffinityC.class;
            case TASK_AFFINITY_NONE:
            default:
                return SingleTopActivity.class;
        }
    }

    private Class<? extends BaseActivity> getSingleTaskActivityClassByTaskAffinity() {
        switch (mTargetAffinity) {
            case TASK_AFFINITY_A:
                return SingleTaskActivity_AffinityA.class;
            case TASK_AFFINITY_B:
                return SingleTaskActivity_AffinityB.class;
            case TASK_AFFINITY_C:
                return SingleTaskActivity_AffinityC.class;
            case TASK_AFFINITY_NONE:
            default:
                return SingleTaskActivity.class;
        }
    }

    private Class<? extends BaseActivity> getSingleInstanceActivityClassByTaskAffinity() {
        switch (mTargetAffinity) {
            case TASK_AFFINITY_A:
                return SingleInstanceActivity_AffinityA.class;
            case TASK_AFFINITY_B:
                return SingleInstanceActivity_AffinityB.class;
            case TASK_AFFINITY_C:
                return SingleInstanceActivity_AffinityC.class;
            case TASK_AFFINITY_NONE:
            default:
                return SingleInstanceActivity.class;
        }
    }

    private void startActivity(Class<? extends BaseActivity> target) {
        try {
            startActivity(prepareIntent(target));
        } catch (Exception e) {
            Toast.makeText(this, String.format(Locale.ENGLISH, "启动失败: %s", e.getMessage()), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void startActivityForResult(Class<? extends BaseActivity> target) {
        try {
            startActivityForResult(prepareIntent(target), hashCode() & 0xFFFF);
        } catch (Exception e) {
            Toast.makeText(this, String.format(Locale.ENGLISH, "启动失败: %s", e.getMessage()), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private Intent prepareIntent(Class<? extends BaseActivity> target) {
        Intent intent = new Intent(this, target);
        intent.setFlags(collectFlags());
        intent.putExtra("sv_x", svFlagsContainer.getScrollX());
        intent.putExtra("sv_y", svFlagsContainer.getScrollY());
        intent.putExtra("affinity_cur", mTargetAffinity);
        return intent;
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
