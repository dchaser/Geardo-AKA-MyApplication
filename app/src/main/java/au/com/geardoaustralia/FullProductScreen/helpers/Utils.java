package au.com.geardoaustralia.FullProductScreen.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by codeninja on 7/4/2015.
 */
public class Utils {

    private Context _context;
    AlertDialog localBuilder;

    public Utils(Context paramContext)
    {
        this._context = paramContext;
    }

    private boolean IsSupportedFile(String paramString)
    {
        String str = paramString.substring(1 + paramString.lastIndexOf("."), paramString.length());
        return AppConstant.FILE_EXTN.contains(str.toLowerCase(Locale.getDefault()));
    }


    public ArrayList<String> getFilePaths(String folderName, String thumbOrLarge) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            String[] fileList = this._context.getAssets().list(folderName);
            if (fileList.length <= 0) {
                //if no files found

                new AlertDialog.Builder(this._context)
                        .setTitle("Error!")
                        .setMessage("No image directory in assets folder for the selected item")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }


            for (int i = 0; i < fileList.length; i++) {

                if (thumbOrLarge.equals("thumbnail")) {

                    if (fileList[i].contains("_t")) {
                        String fullPath = folderName + "/" + fileList[i];
                        list.add(fullPath);
                    }
                }

                if (thumbOrLarge.equals("large")) {

                    if (!fileList[i].contains("_t")) {
                        String fullPath = folderName + "/" + fileList[i];
                        list.add(fullPath);
                    }
                }
            }


        } catch (Exception ex) {

            ex.toString();
        }

        return list;
    }


    public int getScreenWidth()
    {
        Display localDisplay = ((WindowManager)this._context.getSystemService(this._context.WINDOW_SERVICE)).getDefaultDisplay();
        Point localPoint = new Point();
        try
        {
            localDisplay.getSize(localPoint);
            return localPoint.x;
        }
        catch (NoSuchMethodError localNoSuchMethodError)
        {
            for (;;)
            {
                localPoint.x = localDisplay.getWidth();
                localPoint.y = localDisplay.getHeight();
            }
        }
    }
}
