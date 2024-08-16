package com.example.sqlitesotiroglou;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sqlitesotiroglou.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    DatabaseHelper myDb;

    EditText editName, editSurname, editMarks, editId;
    Button btnAddData, btnViewAllData, btnUpdateData, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        myDb = new DatabaseHelper(this);
        editName = (EditText) findViewById((R.id.editTextName));
        editSurname = (EditText) findViewById((R.id.editTextSurname));
        editMarks = (EditText) findViewById((R.id.editTextMarks));
        editId = findViewById(R.id.editTextId);
        btnAddData = findViewById(R.id.button);
        btnViewAllData = findViewById(R.id.btnViewAll);
        btnUpdateData = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        addData();
        viewAll();
        updateData();
        deleteData();
    }

    public void addData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editSurname.getText().toString(),
                                editMarks.getText().toString());

                        if (isInserted) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void viewAll() {
        btnViewAllData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            // show message
                            showMessage("Error", "No rows found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()) {
                            buffer.append("Id : " + res.getString(0) +"\n");
                            buffer.append("Name : " + res.getString(1) +"\n");
                            buffer.append("Surname : " + res.getString(2) +"\n");
                            buffer.append("Marks : " + res.getString(3) +"\n\n");
                        }

                        // show all data
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void updateData() {
        btnUpdateData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isUpdated = myDb.updateData(
                                editId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getText().toString(),
                                editMarks.getText().toString()
                        );

                        if (isUpdated) {
                            Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Data Not Updated", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    public void deleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deletedRows = myDb.deleteData(editId.getText().toString());
                        if (deletedRows > 0) {
                            Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Data Not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}