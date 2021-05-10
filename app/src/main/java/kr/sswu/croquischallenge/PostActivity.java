package kr.sswu.croquischallenge;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

import kr.sswu.croquischallenge.login.activity.LoginActivity;

public class PostActivity extends AppCompatActivity {

    final private static String TAG = "";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    private String name, email, uid;
    private String imageUri, imageRefUri;
    private String downloadSrcUri;

    private TextView toolBarTitle;
    private ImageView btn_back, imageView;

    private ProgressDialog progressDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //get user info
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        checkUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users");
        Query query = reference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    name = ds.child("name").getValue().toString();
                    email = ds.child("email").getValue().toString();
                    uid = ds.child("uid").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        imageView = (ImageView) findViewById(R.id.imageView);

        //title
        btn_title = (ImageButton) findViewById(R.id.btn_title);
        edit_title = (EditText) findViewById(R.id.edit_title);
        //description
        btn_description = (ImageButton) findViewById(R.id.btn_description);
        edit_description = (EditText) findViewById(R.id.edit_description);
        //date
        btn_date = (ImageButton) findViewById(R.id.btn_date);
        edit_date = (EditText) findViewById(R.id.edit_date);
        //category
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteText);

        buttonUpload = (Button) findViewById(R.id.btn_upload);
        progressDialog = new ProgressDialog(this);

        imageUri = getIntent().getStringExtra("image");
        imageRefUri = getIntent().getStringExtra("ref");

        toolBarTitle.setText("New Post");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] category = {"Anatomy", "Animal", "Objects", "Scenery"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, category);
        autoCompleteTextView.setAdapter(arrayAdapter);


        imageView.setImageURI(Uri.parse(imageUri));

        btn_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = new EditText(PostActivity.this);
                edit.setPadding(70, 30, 70, 30);
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
                edit.setPadding(70, 30, 70, 30);
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
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageRefUri.equals("null"))
                    uploadToFirebase(Uri.parse(imageUri));
                else
                    uploadToFirebase(Uri.parse(imageUri), Uri.parse(imageRefUri));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }

    private void checkUser() {
        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
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
                        while (!uriTask.isSuccessful()) ;

                        String downloadUri = uriTask.getResult().toString();

                        if (uriTask.isSuccessful()) {
                            HashMap<String, Object> feed = new HashMap<>();
                            feed.put("uid", uid);
                            feed.put("uName", name);
                            feed.put("email", email);
                            feed.put("upload_time", fTime);
                            feed.put("image", downloadUri);
                            feed.put("ref", "");
                            feed.put("title", edit_title.getText().toString());
                            feed.put("description", edit_description.getEditableText().toString());
                            feed.put("date", edit_date.getText().toString());
                            feed.put("category", autoCompleteTextView.getEditableText().toString());

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
                            ref.child(timeStamp).setValue(feed)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PostActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                        }
                                    });
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressDialog.setMessage("Upload..");
                        progressDialog.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //feed upload 후 Feed 메인 회면으로 전환
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    private void uploadToFirebase(Uri uri, Uri srcUri) {
        String timeStamp = String.valueOf(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String fTime = DateFormat.format("yyyy-MM-dd hh:mm aa", calendar).toString();

        String filePathName = "Feeds/" + "feeds_" + timeStamp;
        String fileSrcPathName = "Feeds/" + "src_" + timeStamp;

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathName);

        StorageReference refSrc = FirebaseStorage.getInstance().getReference().child(fileSrcPathName);
        refSrc.putFile(srcUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;

                        downloadSrcUri = uriTask.getResult().toString();

                        if (uriTask.isSuccessful()) {
                            ref.putFile(uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isSuccessful()) ;

                                            String downloadUri = uriTask.getResult().toString();

                                            if (uriTask.isSuccessful()) {
                                                HashMap<String, Object> feed = new HashMap<>();
                                                feed.put("uid", uid);
                                                feed.put("uName", name);
                                                feed.put("email", email);
                                                feed.put("upload_time", fTime);
                                                feed.put("image", downloadUri);
                                                feed.put("ref", downloadSrcUri);
                                                feed.put("title", edit_title.getText().toString());
                                                feed.put("description", edit_description.getEditableText().toString());
                                                feed.put("date", edit_date.getText().toString());
                                                feed.put("category", autoCompleteTextView.getEditableText().toString());

                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
                                                ref.child(timeStamp).setValue(feed)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(PostActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                            }
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            progressDialog.setMessage("Upload..");
                                            progressDialog.show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "src UriTask failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //feed upload 후 Feed 메인 회면으로 전환
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}