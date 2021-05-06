package kr.sswu.croquischallenge;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_ACTION_CODE = 1;
    private static final int CAMERA_ACTION_CODE = 2;

    private TextView toolBarTitle;
    private ImageView buttonClose, imageView;
    private BottomSheetDialog bottomSheetDialog;
    private ProgressBar progressBar;

    //title
    private EditText edit_title;
    private ImageButton btn_title;
    //description
    private ImageButton btn_description;
    private EditText edit_description;
    //date
    private ImageButton btn_date;
    private EditText edit_date;
    //category
    private AutoCompleteTextView autoCompleteTextView;

    private Button buttonUpload;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference reference = storage.getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri imageUri;

    final private static String TAG = "";
    String mCurrentPhotoPath;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        buttonClose = (ImageView)findViewById(R.id.close_button);

        imageView = (ImageView)findViewById(R.id.add_photo);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //title
        btn_title = (ImageButton) findViewById(R.id.btn_title);
        edit_title = (EditText)findViewById(R.id.edit_title);
        //description
        btn_description = (ImageButton) findViewById(R.id.btn_description);
        edit_description = (EditText) findViewById(R.id.edit_description);
        //date
        btn_date = (ImageButton) findViewById(R.id.btn_date);
        edit_date = (EditText)findViewById(R.id.edit_date);
        //category
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteText);

        buttonUpload = (Button)findViewById(R.id.upload_button);

        progressBar.setVisibility(View.INVISIBLE);
        toolBarTitle.setText("New Post");
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] category = {"Anatomy", "Animal", "Objects", "Scenery"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, category);
        autoCompleteTextView.setAdapter(arrayAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(PostActivity.this, R.style.BottomSheetTheme);

                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout,
                        (ViewGroup) findViewById(R.id.bottom_sheet));

                sheetView.findViewById(R.id.getCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //카메라로 사진 가져오기
                        dispatchTakePhotoIntent();
                    }
                });

                sheetView.findViewById(R.id.getGallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 앨범에서 사진 가져오기
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_ACTION_CODE);

                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });

        btn_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = new EditText(PostActivity.this);
                edit.setPadding(70,30,70,30);
                edit.setSingleLine();
                edit.setText(edit_title.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setTitle("Title");
                builder.setMessage("Enter the title of the work");
                builder.setView(edit);
                builder.setPositiveButton("입력",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                edit_title.setText(edit.getText().toString());
                                edit_title.setTextColor(Color.DKGRAY);
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });

        btn_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = new EditText(PostActivity.this);
                edit.setPadding(70,30,70,30);
                edit.setText(edit_description.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setTitle("Description");
                builder.setMessage("Enter the description of the work");
                builder.setView(edit);
                builder.setPositiveButton("입력",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                edit_description.setText(edit.getText().toString());
                                edit_description.setTextColor(Color.DKGRAY);
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        int month = m + 1;
                        edit_date.setTextColor(Color.DKGRAY);
                        edit_date.setText(y + "-" + month + "-" + d);
                    }}, year, month, day);
                datePickerDialog.show();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(PostActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //앨범에서 이미지 가져오는 경우
        if (requestCode == GALLERY_ACTION_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        // 카메라로 사진을 찍어 이미지 가져오는 경우
        if (requestCode == CAMERA_ACTION_CODE && resultCode == RESULT_OK) {
            try {
                File file = new File(mCurrentPhotoPath);
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));

                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                        if (bitmap != null) {
                            CropImage.activity(imageUri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            CropImage.activity(imageUri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(this);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                imageView.setBackgroundColor(Color.WHITE);
                imageView.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void dispatchTakePhotoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(this, "이미지 처리 오류 발생. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            ex.printStackTrace();
        }
        if (photoFile != null) {
            imageUri = FileProvider.getUriForFile(this, "kr.sswu.croquischallenge.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_ACTION_CODE);
            bottomSheetDialog.dismiss();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadToFirebase(Uri uri) {
        String timeStamp = String.valueOf(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String fTime = DateFormat.format("yyyy-MM-dd hh:mm aa", calendar).toString();

        String filePathName = "Feeds/" + "feeds_" + timeStamp;

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathName);
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());

                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()) {
                            HashMap<String, Object> feed = new HashMap<>();
                            feed.put("upload_time", fTime);
                            feed.put("image", downloadUri);
                            feed.put("title", edit_title.getText().toString());
                            feed.put("description", edit_description.getEditableText().toString());
                            feed.put("date", edit_date.getText().toString());
                            feed.put("category", autoCompleteTextView.getEditableText().toString());

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
                            ref.child(timeStamp).setValue(feed)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(PostActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
         //feed upload 후 Feed 메인 회면으로 전환
        startActivity(new Intent(PostActivity.this, MainActivity.class));
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}