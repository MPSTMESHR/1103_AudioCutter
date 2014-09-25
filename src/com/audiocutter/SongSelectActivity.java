
package com.audiocutter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.audiocutter.R;
import com.audiocutter.soundfile.CheapSoundFile;

import java.io.File;
import java.util.ArrayList;


public class SongSelectActivity
    extends ListActivity
    
{
    private TextView mFilter;
    SimpleCursorAdapter adapter;
    private boolean mWasGetContentIntent;
   

   

    public SongSelectActivity() {
    }

  
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.media_select);

      
        String from[] = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DATA};
		int[] to = {R.id.row_title};

		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, from, null, null,
				null);

		adapter = new SimpleCursorAdapter(this, R.layout.media_select_row, cursor, from, to);
		setListAdapter(adapter);
      
            getListView().setItemsCanFocus(true);

            
            getListView().setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView parent,
                        View view,
                        int position,
                        long id) {
                    startEditor();
                }                         
            });

    }
    

   

    
  
    private Uri getUri(){
        //Get the uri of the item that is in the row
        Cursor c = adapter.getCursor();
        int uriIndex = c.getColumnIndex(
                "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\"");
        if (uriIndex == -1) {
            uriIndex = c.getColumnIndex(
                    "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\"");
        }
        String itemUri = c.getString(uriIndex) + "/" +
        c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        return (Uri.parse(itemUri));
    }

   

   
    private void startEditor() {
        Cursor c = adapter.getCursor();
        int dataIndex = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        String filename = c.getString(dataIndex);
       
            Intent intent = new Intent(Intent.ACTION_EDIT,
                    Uri.parse(filename));
            intent.putExtra("was_get_content_intent",
                    mWasGetContentIntent);
            intent.setClassName(
                    "com.audiocutter",
            "com.audiocutter.SongEditActivity");
            startActivity(intent);
      
    }

    private Cursor getInternalAudioCursor(String selection,
            String[] selectionArgs) {
        return managedQuery(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                INTERNAL_COLUMNS,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    private Cursor getExternalAudioCursor(String selection,
            String[] selectionArgs) {
        return managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                EXTERNAL_COLUMNS,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    private static final String[] INTERNAL_COLUMNS = new String[] {
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.IS_RINGTONE,
        MediaStore.Audio.Media.IS_ALARM,
        MediaStore.Audio.Media.IS_NOTIFICATION,
        MediaStore.Audio.Media.IS_MUSIC,
        "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\""
    };

    private static final String[] EXTERNAL_COLUMNS = new String[] {
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.IS_RINGTONE,
        MediaStore.Audio.Media.IS_ALARM,
        MediaStore.Audio.Media.IS_NOTIFICATION,
        MediaStore.Audio.Media.IS_MUSIC,
        "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\""
    };
}
