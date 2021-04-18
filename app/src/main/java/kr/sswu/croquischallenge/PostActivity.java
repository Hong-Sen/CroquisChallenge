package kr.sswu.croquischallenge;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_ACTION_CODE = 1;
    private static final int CAMERA_ACTION_CODE = 2;

    private TextView toolBarTitle;
    private ImageView buttonClose, imageView;
    private BottomSheetDialog bottomSheetDialog;
    private ProgressBar progressBar;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText editText;
    private Button buttonUpload;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference reference = storage.getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri imageUri;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolBarTitle = findViewById(R.id.toolbar_title);
        buttonClose = findViewById(R.id.close_button);

        imageView = findViewById(R.id.add_photo);
        progressBar = findViewById(R.id.progressBar);
        autoCompleteTextView = findViewById(R.id.autoCompleteText);
        editText = findViewById(R.id.description);
        buttonUpload = findViewById(R.id.upload_button);

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

        //default value setting
        //   autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false);
        autoCompleteTextView.setAdapter(arrayAdapter);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    startCropActivity();

                bottomSheetDialog = new BottomSheetDialog(PostActivity.this, R.style.BottomSheetTheme);

                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout,
                        (ViewGroup) findViewById(R.id.bottom_sheet));

                sheetView.findViewById(R.id.getCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //카메라로 사진 찍기 구현
                        
                        Toast.makeText(PostActivity.this, "Camera cannot be used yet. \n" +
                                "Please bring a photo from the gallery", Toast.LENGTH_LONG).show();



                        /*
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, CAMERA_ACTION_CODE);
                        } else {
                            Toast.makeText(PostActivity.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                        }

                         */
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

    /*
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK && data != null) {
                imageUri = data.getData();
                imageView.setBackgroundColor(Color.WHITE);
                imageView.setImageURI(imageUri);
            }

        }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ACTION_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
/*
        if (requestCode == CAMERA_ACTION_CODE && requestCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap photo = (Bitmap)bundle.get("data");
            imageView.setBackgroundColor(Color.WHITE);
            imageView.setImageBitmap(photo);
        }
*/
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

    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Create a new feed with a feedId, category and description
                        Map<String, Object> feed = new HashMap<>();
                        feed.put("feedId", uri.toString());
                        feed.put("category", autoCompleteTextView.getEditableText().toString());
                        feed.put("description", editText.getEditableText().toString());

                        db.collection("feeds")
                                .add(feed)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        //    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //   Log.w(TAG, "Error adding document", e);
                                    }
                                });

                        Toast.makeText(PostActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        //feed upload 후 Feed 메인 회면으로 전환
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostActivity.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}