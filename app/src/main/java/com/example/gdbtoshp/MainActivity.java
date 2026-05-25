package com.example.gdbtoshp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    private static final int REQUEST_GDB_FILE = 200;
    private static final int REQUEST_OUTPUT_DIR = 300;
    private static final int REQUEST_MANAGE_STORAGE = 400;

    private Button selectGdbButton;
    private Button selectOutputButton;
    private Button convertButton;
    private TextView gdbFileText;
    private TextView outputDirText;
    private TextView statusText;
    private ProgressBar progressBar;

    private File gdbFile;
    private File outputDir;
    private GDBToSHPConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupConverter();
        checkPermissions();
    }

    private void initViews() {
        selectGdbButton = findViewById(R.id.selectGdbButton);
        selectOutputButton = findViewById(R.id.selectOutputButton);
        convertButton = findViewById(R.id.convertButton);
        gdbFileText = findViewById(R.id.gdbFileText);
        outputDirText = findViewById(R.id.outputDirText);
        statusText = findViewById(R.id.statusText);
        progressBar = findViewById(R.id.progressBar);

        selectGdbButton.setOnClickListener(v -> selectGdbFile());
        selectOutputButton.setOnClickListener(v -> selectOutputDir());
        convertButton.setOnClickListener(v -> startConversion());
    }

    private void setupConverter() {
        converter = new GDBToSHPConverter();
        converter.setListener(new GDBToSHPConverter.ConversionListener() {
            @Override
            public void onProgress(int current, int total) {
                runOnUiThread(() -> {
                    String progress = getString(R.string.progress, current, total);
                    statusText.setText(progress);
                });
            }

            @Override
            public void onComplete(String outputPath) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    convertButton.setEnabled(true);
                    statusText.setText(R.string.conversion_complete);
                    Toast.makeText(MainActivity.this, R.string.conversion_complete, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    convertButton.setEnabled(true);
                    statusText.setText(error);
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
            }
        } else {
            String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            
            boolean needsPermission = false;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    needsPermission = true;
                    break;
                }
            }
            
            if (needsPermission) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }
    }

    private void selectGdbFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_GDB_FILE);
    }

    private void selectOutputDir() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_OUTPUT_DIR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
                
                String path = uri.getPath();
                if (path != null) {
                    File file = new File(path.replace("/tree/", "/"));
                    
                    if (requestCode == REQUEST_GDB_FILE) {
                        gdbFile = file;
                        gdbFileText.setText(getString(R.string.gdb_file, gdbFile.getName()));
                    } else if (requestCode == REQUEST_OUTPUT_DIR) {
                        outputDir = file;
                        outputDirText.setText(getString(R.string.output_dir, outputDir.getAbsolutePath()));
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "需要存储权限才能使用此应用", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startConversion() {
        if (gdbFile == null || outputDir == null) {
            Toast.makeText(this, R.string.please_select_files, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        convertButton.setEnabled(false);
        statusText.setText(R.string.converting);
        
        converter.convert(gdbFile, outputDir);
    }
}
