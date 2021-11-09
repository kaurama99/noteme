package com.uoit.noteme.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.uoit.noteme.R;
import com.uoit.noteme.database.NotesDatabase;
import com.uoit.noteme.entities.Note;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;

    ImageView noteImg;
    private String selectedNoteColor;
    private static final int SELECT_PICTURE = 252;
    private byte[] selectedImageBytes = new byte[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNoteText);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        noteImg = findViewById(R.id.imgActivity);
        textDateTime = findViewById(R.id.textDateTime);
        textDateTime.setText(new SimpleDateFormat(
                "EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date().getTime())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v -> saveNote());

        selectedNoteColor = "#333333";

        initMiscellaneous();
        setSubtitleIndicatorColor();

        //addImageButton
        ImageButton imageButton = findViewById(R.id.addImageButton);
        imageButton.setOnClickListener(v -> selectImage());
    }

    private void saveNote() {
        final String noteTitle = inputNoteTitle.getText().toString().trim();
        final String noteSubtitle = inputNoteSubtitle.getText().toString().trim();
        final String noteText = inputNoteText.getText().toString().trim();
        final String dateTimeStr = textDateTime.getText().toString().trim();

        if (noteTitle.isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();
        note.setTitle(noteTitle);
        note.setSubtitle(noteSubtitle);
        note.setNoteText(noteText);
        note.setDateTime(dateTimeStr);
        note.setColor(selectedNoteColor);

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNoteTask().execute();
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutColor);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(v -> {
            selectedNoteColor = "#333333";
            imageColor1.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(v -> {
            selectedNoteColor = "#FFFF00";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(R.drawable.ic_done);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(v -> {
            selectedNoteColor = "#7B99F4";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(R.drawable.ic_done);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(v -> {
            selectedNoteColor = "#98D319";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(R.drawable.ic_done);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(v -> {
            selectedNoteColor = "#19CED3";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(R.drawable.ic_done);
            setSubtitleIndicatorColor();
        });
    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
    public void selectImage(){
        boolean havePermission = checkPermissionForReadExternalStorage();
        Log.d("DEBUG",  "Have storage permission: " + String.valueOf(havePermission));
        if (havePermission){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
        }
    }

    //Checks if pictures to access files was granted
    private boolean checkPermissionForReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
            return true;
        } else {
            //Permission not granted, request it
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(perms, 1);
            return false;
        }
    }

    //Handle saving of pictures to var selectedImageBytes
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();
                ContentResolver cr = getBaseContext().getContentResolver();
                try {
                    InputStream inputStream;
                    inputStream = cr.openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    selectedImageBytes = baos.toByteArray();

                    // Display image
                    noteImg.setImageBitmap(getImage(selectedImageBytes));
                    noteImg.setVisibility(View.VISIBLE);
                    Log.d("DEBUG", "Image with size of " + String.valueOf(selectedImageBytes.length) + " bytes saved");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults){
        for (int i = 0; i < permissions.length; i++) {
            Log.d("DEBUG", "Permission " + permissions[i] + " " + grantResults[i]);
            if (Objects.equals(permissions[i], Manifest.permission.READ_EXTERNAL_STORAGE)){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this,"Permission required to access files", Toast.LENGTH_SHORT).show();
                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    selectImage();
                }
            }
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
