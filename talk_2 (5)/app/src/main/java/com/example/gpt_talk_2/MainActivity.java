package com.example.gpt_talk_2;

import android.Manifest;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    TextView chatView;
    EditText editText;
    Button button;
    Button imageButton;
    ImageView imageView;
    ScrollView scrollView;
    Socket socket;
    DataOutputStream out;
    BufferedReader in;
    String username;
    String chatname;
    String imgString = "";
    private File file;
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private static final int PERMISSION_REQUEST_CODE = 0;
    public static final int PICK_IMAGE_REQUEST = 1;

    private static final String IMAGE_END = "IMAGE_END:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File externalDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(externalDir, "capture.jpg");

        chatView = findViewById(R.id.chatView);
        editText = findViewById(R.id.message);
        button = findViewById(R.id.chatbutton);
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);
        scrollView = findViewById(R.id.scrollView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        chatname = intent.getStringExtra("chatname");
        getSupportActionBar().setTitle(chatname);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = editText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            out.write((username + ": " + str + "\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                editText.setText("");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setSocket("172.20.10.4", 8888);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
    }

    private class ImageProcessTask extends AsyncTask<Void, Void, String> {
        private Bitmap bitmap;

        public ImageProcessTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }

        @Override
        protected void onPostExecute(String result) {
            imgString = result;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (out != null) {
                            out.write(("IMAGE:" + imgString + "\n").getBytes());
                            out.write((IMAGE_END + "\n").getBytes());
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Connection is not established yet", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    void setSocket(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write((username + " connected\n").getBytes());

            MyThread myThread = new MyThread(socket);
            myThread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    class MyThread extends Thread {
        Socket socket;
        StringBuilder partialBase64Image = new StringBuilder();
        boolean isReceivingImage = false;

        MyThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    final String receivedMsg = message;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (receivedMsg.startsWith("IMAGE:")) {
                                isReceivingImage = true;
                                String base64Image = receivedMsg.substring(6);
                                partialBase64Image.append(base64Image);
                            } else if (isReceivingImage) {
                                if (receivedMsg.equals(IMAGE_END)) {
                                    // 이미지 데이터의 끝을 받았으므로 디코딩하여 표시
                                    String finalBase64Image = partialBase64Image.toString();
                                    byte[] decodedString = Base64.decode(finalBase64Image, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    imageView.setImageBitmap(decodedByte);
                                    partialBase64Image = new StringBuilder();
                                    isReceivingImage = false;
                                } else {
                                    // 이미지 데이터 계속 수신 중
                                    partialBase64Image.append(receivedMsg);
                                }
                            } else {
                                // 텍스트 메시지 처리
                                chatView.append("\n" + receivedMsg);
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void capture() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        }
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되었습니다. 이제 외부 저장소에 접근할 수 있습니다.
            } else {
                // 권한이 거부되었습니다. 권한이 필요한 기능을 사용할 수 없습니다.
            }
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.gpt_talk_2.fileprovider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void detectFaceExpression(Bitmap bitmap) {
            try {
                String modelFilename = "face_mesh.tflite";
                AssetManager assetManager = getAssets();

                // 1. 모델 파일 열기
                InputStream inputStream = assetManager.open(modelFilename);
                int modelSize = inputStream.available();
                byte[] modelBuffer = new byte[modelSize];
                inputStream.read(modelBuffer);
                inputStream.close();

                // 2. TensorFlow Lite 모델 로드
                ByteBuffer tfliteModel = ByteBuffer.allocateDirect(modelSize);
                tfliteModel.put(modelBuffer);
                tfliteModel.rewind();

                // 3. TensorFlow Lite 옵션 설정
                Interpreter.Options options = new Interpreter.Options();
                Interpreter interpreter = new Interpreter(tfliteModel, options);

                // 4. 입력 및 출력 텐서 가져오기
                Tensor inputTensor = interpreter.getInputTensor(0);
                Tensor outputTensor = interpreter.getOutputTensor(0);

                // 5. 입력 데이터 준비
                int inputWidth = 192;
                int inputHeight = 192;
                int numChannels = 3;
                int batchSize = 1;
                int[] inputShape = {batchSize, inputWidth, inputHeight, numChannels};
                DataType inputDataType = inputTensor.dataType();
                TensorBuffer inputBuffer = TensorBuffer.createFixedSize(inputShape, inputDataType);
                preprocessImage(bitmap, inputWidth, inputHeight, numChannels, inputBuffer);

                // 6. 모델 추론 수행
                DataType outputDataType = outputTensor.dataType();
                int[] outputShape = outputTensor.shape();
                TensorBuffer outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType);
                interpreter.run(inputBuffer.getBuffer(), outputBuffer.getBuffer());
                float[] outputValues = outputBuffer.getFloatArray();
                float[] xyValues = new float[outputValues.length / 3 * 2];
                int xyIndex = 0;
                float base_x = 0;
                float base_y = 0;
                for (int i = 0; i < outputValues.length; i += 3) {
                    float x = outputValues[i];
                    float y = outputValues[i + 1];

                    // Convert to relative coordinates
                    if (xyIndex == 0) {
                        base_x = x;
                        base_y = y;
                    }
                    xyValues[xyIndex++] = x - base_x;  // 상대적인 x 좌표
                    xyValues[xyIndex++] = y - base_y;  // 상대적인 y 좌표
                }
                float maxX = 0;
                for (int i = 0; i < xyValues.length; i += 2) {
                    float x = xyValues[i];
                    // 최댓값 갱신
                    if (Math.abs(x) > maxX) {
                        maxX = Math.abs(x);
                    }
                }

                float maxY = 0;
// 배열 순회하여 최댓값 찾기
                for (int i = 1; i < xyValues.length; i += 2) {
                    float y = xyValues[i];
                    // 최댓값 갱신
                    if (Math.abs(y) > maxY) {
                        maxY = Math.abs(y);
                    }
                }

                Log.d("Max Values", "Max X: " + maxX);
                Log.d("Max Values", "Max Y: " + maxY);

                float[] normalizedXYValues = new float[xyValues.length];
// 정규화된 좌표값 계산
                for (int i = 0; i < xyValues.length; i += 2) {
                    float x = xyValues[i];
                    float y = xyValues[i + 1];

                    // 좌표값 정규화
                    float normalizedX = x / maxX;
                    float normalizedY = y / maxY;

                    // 정규화된 좌표값 저장
                    normalizedXYValues[i] = normalizedX;
                    normalizedXYValues[i + 1] = normalizedY;
                }
                for (int i = 0; i < normalizedXYValues.length; i += 2) {
                    float normalizedX = normalizedXYValues[i];
                    float normalizedY = normalizedXYValues[i + 1];
                    Log.d("Normalized Coordinates", "X: " + normalizedX + ", Y: " + normalizedY);
                }
                detectFaceExpression_1(normalizedXYValues);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    private void detectFaceExpression_1(float[] xyValues) {
        try {
            String modelFilename = "model_4.tflite";
            AssetManager assetManager = getAssets();

            // 1. 모델 파일 열기
            InputStream inputStream = assetManager.open(modelFilename);
            int modelSize = inputStream.available();
            byte[] modelBuffer = new byte[modelSize];
            inputStream.read(modelBuffer);
            inputStream.close();

            // 2. TensorFlow Lite 모델 로드
            ByteBuffer tfliteModel = ByteBuffer.allocateDirect(modelSize);
            tfliteModel.put(modelBuffer);
            tfliteModel.rewind();

            // 3. TensorFlow Lite 옵션 설정
            Interpreter.Options options = new Interpreter.Options();
            Interpreter interpreter = new Interpreter(tfliteModel, options);

            // 4. 입력 및 출력 텐서 가져오기
            Tensor inputTensor = interpreter.getInputTensor(0);
            Tensor outputTensor = interpreter.getOutputTensor(0);
            DataType dataType = DataType.FLOAT32; // Assuming the data type is float
            int[] shape = {1, xyValues.length}; // Assuming the shape is [1, length of xyValues]
            TensorBuffer tensorBuffer = TensorBuffer.createFixedSize(shape, dataType);
            // 5. 입력 데이터 준비
            int[] inputShape = inputTensor.shape();
            ByteBuffer inputBuffer = ByteBuffer.allocateDirect(xyValues.length * 4);
            inputBuffer.order(ByteOrder.nativeOrder());
            for (float value : xyValues) {
                inputBuffer.putFloat(value);
            }
            inputBuffer.rewind();
            tensorBuffer.loadBuffer(inputBuffer);

            DataType outputDataType = outputTensor.dataType();
            int[] outputShape = outputTensor.shape();
            TensorBuffer outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType);

            // 6. 모델 추론 수행
            interpreter.run(tensorBuffer.getBuffer(), outputBuffer.getBuffer());

            // 7. 출력 버퍼에서 결과 가져오기
            float[] outputValues = new float[outputTensor.shape()[1]];

            // 8. 결과 출력
            int numClasses = outputShape[outputShape.length - 1];
            int maxIndex = 0;
            float maxValue = outputBuffer.getFloatValue(0);

            for (int i = 0; i < numClasses; i++) {
                float value = outputBuffer.getFloatValue(i);
                if (value > maxValue) {
                    maxValue = value;
                    maxIndex = i;
                }
            }
            if (maxIndex == 0) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // 권한이 이미 허용된 경우 파일에 접근할 수 있습니다.
                    // 파일을 열거나 사용하는 코드를 여기에 작성하세요.
                    String imageFilePath = "/sdcard/DCIM/Camera/happy.png";  // 가져올 이미지 파일 경로
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                    if (imageBitmap != null) {
                        // Convert the captured image to a Base64-encoded string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        // Send the image through the socket
                        if (out != null) {
                            SendImageTask sendImageTask = new SendImageTask();
                            sendImageTask.execute(encodedImage);
                            //out.write((username + " 행복^^\n").getBytes());
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Connection is not established yet", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }

            else if (maxIndex == 1) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // 권한이 이미 허용된 경우 파일에 접근할 수 있습니다.
                    // 파일을 열거나 사용하는 코드를 여기에 작성하세요.
                    String imageFilePath = "/sdcard/DCIM/Camera/neutral.png";  // 가져올 이미지 파일 경로
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                    if (imageBitmap != null) {
                        // Convert the captured image to a Base64-encoded string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        // Send the image through the socket
                        if (out != null) {
                            SendImageTask sendImageTask = new SendImageTask();
                            sendImageTask.execute(encodedImage);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Connection is not established yet", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
            else if (maxIndex == 2) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // 권한이 이미 허용된 경우 파일에 접근할 수 있습니다.
                    // 파일을 열거나 사용하는 코드를 여기에 작성하세요.
                    String imageFilePath = "/sdcard/DCIM/Camera/sad.png";  // 가져올 이미지 파일 경로
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                    if (imageBitmap != null) {
                        // Convert the captured image to a Base64-encoded string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        // Send the image through the socket
                        if (out != null) {
                            SendImageTask sendImageTask = new SendImageTask();
                            sendImageTask.execute(encodedImage);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Connection is not established yet", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
            else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // 권한이 이미 허용된 경우 파일에 접근할 수 있습니다.
                    // 파일을 열거나 사용하는 코드를 여기에 작성하세요.
                    String imageFilePath = "/sdcard/DCIM/Camera/surprise.png";  // 가져올 이미지 파일 경로
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                    if (imageBitmap != null) {
                        // Convert the captured image to a Base64-encoded string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        // Send the image through the socket
                        if (out != null) {
                            SendImageTask sendImageTask = new SendImageTask();
                            sendImageTask.execute(encodedImage);
                            //out.write((username + " 놀람00\n").getBytes());
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Connection is not established yet", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 이미지를 서버로 보내는 AsyncTask 클래스
    private class SendImageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String encodedImage = params[0];

            // Send the image through the socket
            try {
                if (out != null) {
                    out.write(("IMAGE:" + encodedImage + "\n").getBytes());
                    out.write((IMAGE_END + "\n").getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 작업이 완료되었을 때 수행할 동작 추가 (예: 결과 처리)
        }
    }




    // 이미지 전처리 메서드 예시
    private void preprocessImage(Bitmap bitmap, int inputWidth, int inputHeight, int numChannels, TensorBuffer inputBuffer) {
        // 1. 입력 이미지를 지정된 크기로 조정
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true);

        // 2. 이미지의 픽셀 값을 가져와서 입력 버퍼에 채우기
        int[] intValues = new int[inputWidth * inputHeight];
        float[] floatValues = new float[inputWidth * inputHeight * numChannels];

        resizedBitmap.getPixels(intValues, 0, inputWidth, 0, 0, inputWidth, inputHeight);
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * numChannels] = (val >> 16) & 0xFF; // Red 채널
            floatValues[i * numChannels + 1] = (val >> 8) & 0xFF; // Green 채널
            floatValues[i * numChannels + 2] = val & 0xFF; // Blue 채널
        }

        // 3. 입력 버퍼에 값 할당
        inputBuffer.loadArray(floatValues);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                // 이미지 전처리
                // 모델 추론
                /*
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // 권한이 이미 허용된 경우 파일에 접근할 수 있습니다.
                    // 파일을 열거나 사용하는 코드를 여기에 작성하세요.
                    Log.d("Tag", "두둥등장");
                    String imageFilePath = "/sdcard/DCIM/Camera/ho.jpg";  // 가져올 이미지 파일 경로
                    Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);*/
                    detectFaceExpression(bitmap);
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




