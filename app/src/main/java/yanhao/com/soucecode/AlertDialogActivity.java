package yanhao.com.soucecode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-07-21
 * Time: 15:02
 */
public class AlertDialogActivity extends Activity {
    private static final String TAG = "AlertDialogActivity";
    private static final int ALERTTAG = 0, PROGRESSTAG = 1;
    private Button btnShutDown;
    private DialogFragment mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_alertdialog);

        btnShutDown= (Button) findViewById(R.id.shutdownButton);
        btnShutDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment(ALERTTAG);
            }
        });
    }

    // Show desired Dialog
    private void showDialogFragment(int dialogID) {
        switch (dialogID){
            case ALERTTAG:
                mDialogFragment=AlertDialogFragment.newInstance();
                mDialogFragment.show(getFragmentManager(),"Alert");
                break;
            case PROGRESSTAG:
                mDialogFragment=ProgressDialogFragment.newInstance();
                mDialogFragment.show(getFragmentManager(),"Shut Down");
                break;
        }
    }
    // Abort or complete ShutDown based on value of shouldContinue
    private void continueShutDown(boolean shouldContinue){
        if (shouldContinue){
            btnShutDown.setEnabled(false);
            showDialogFragment(PROGRESSTAG);
            finishShutDown();
        }else {
            mDialogFragment.dismiss();
        }
    }

    private void finishShutDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                finish();
            }
        }).start();
    }

    // Class that creates the AlertDialog
    public static class AlertDialogFragment extends DialogFragment{
        public static AlertDialogFragment newInstance(){
            return new AlertDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((AlertDialogActivity)getActivity()).continueShutDown(false);
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((AlertDialogActivity)getActivity()).continueShutDown(true);
                        }
                    }).create();
        }
    }

    // Class that creates the ProgressDialog
    public static class ProgressDialogFragment extends DialogFragment{
        public static ProgressDialogFragment newInstance(){
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            //Create new ProgressDialog
            final ProgressDialog dialog = new ProgressDialog(getActivity());

            // Set Dialog message
            dialog.setMessage("Activity Shutting Down.");

            // Dialog will be displayed for an unknown amount of time
            dialog.setIndeterminate(true);

            return dialog;
        }
    }
}
