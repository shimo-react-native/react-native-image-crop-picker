package com.reactnative.ivpusic.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.reactnative.ivpusic.imagepicker.activity.BasePickerActivity;
import com.yalantis.ucrop.UCrop;

/**
 * Created by song on 2018/1/6.
 */

public class PickerModuleActivity extends BasePickerActivity {
    static final String REQUEST_CODE_KEY = "requestCode";
    static final String IS_PICK_KEY = "pick";
    static final String URI_KEY = "uri";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        boolean pick = intent.getBooleanExtra(IS_PICK_KEY, false);
        int requestCode = intent.getIntExtra(REQUEST_CODE_KEY, -1);
        if (pick) {
            intent.setClass(this, AlbumListActivity.class);
            startActivityForResult(intent, requestCode);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, intent.getParcelableExtra(URI_KEY));
            cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, requestCode);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.null_page;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final PickerModule pickerModule = PickerModule.getModue();
        if (pickerModule == null) {
            finish();
            return;
        }
        final Activity activity = pickerModule.getActivity();
        if (activity == null) {
            finish();
            return;
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            pickerModule.onActivityResult(activity, requestCode, Activity.RESULT_CANCELED, null);
            Toast.makeText(getApplicationContext(), R.string.no_surpport, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (requestCode == PickerModule.CAMERA_PICKER_REQUEST || requestCode == PickerModule.IMAGE_PICKER_REQUEST) {
            pickerModule.onActivityResult(this, requestCode, resultCode, data);
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        } else {
            pickerModule.onActivityResult(activity, requestCode, resultCode, data);
            finish();
        }
    }
}
