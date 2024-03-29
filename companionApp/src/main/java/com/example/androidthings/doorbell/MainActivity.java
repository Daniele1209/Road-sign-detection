/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androidthings.doorbell;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private DoorbellEntryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference for doorbell events from embedded device
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("logs");
        mRecyclerView = (RecyclerView) findViewById(R.id.doorbellView);
        // Show most recent items at the top
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(layoutManager);

        // Initialize RecyclerView adapter
        mAdapter = new DoorbellEntryAdapter(this, ref);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Initialize Firebase listeners in adapter
        mAdapter.startListening();

//        FirebaseStorage storage = FirebaseStorage.getInstance();

//// Create a reference to a file from a Cloud Storage URI
//        StorageReference gsReference = storage.getReferenceFromUrl("gs://signs-2f063.appspot.com/20200813_112940.jpg");
//        File localFile = null;
//        try {
//           localFile = File.createTempFile("images", "jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        gsReference.getFile(localFile).addOnSuccessListener(t -> {
//            System.out.println("image was saved");
//            System.out.println(t);
//        })
//                .addOnFailureListener(t->{});
        // Make sure new events are visible
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

       //  Tear down Firebase listeners in adapter
        mAdapter.stopListening();
    }

}
