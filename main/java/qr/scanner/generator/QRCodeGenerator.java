package qr.scanner.generator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import qr.scanner.generator.room.User;
import qr.scanner.generator.room.UserDatabase;

public class QRCodeGenerator extends AppCompatActivity {

    TextInputEditText editText;
    Button copy, share, btnInsert, btnlistActivity;
    String resultData;
    public static UserDatabase userDatabase;
    private ImageView qrImage;
    private String inputValue;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;
    String uid,name,gender,birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editText = findViewById(R.id.result_edit_text);
        copy = findViewById(R.id.copy);
        share = findViewById(R.id.share);
        btnInsert = findViewById(R.id.btn_insert);
        btnlistActivity = findViewById(R.id.btn_list);

        userDatabase = Room.databaseBuilder(QRCodeGenerator.this, UserDatabase.class, "mylikedb").allowMainThreadQueries().build();
        resultData = getIntent().getStringExtra("result");
        System.out.println("QQQQQQQ : "+resultData);
        editText.setText(resultData);

        btnlistActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRCodeGenerator.this,CardListActivity.class));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String textCopy = resultData + "\n Scan by  \n " + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                ClipData clipQuote = ClipData.newPlainText("Copied", textCopy);
                clipboardManager.setPrimaryClip(clipQuote);
                Toast.makeText(QRCodeGenerator.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, resultData + "\n Scan by \n " +
                        "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "Send Using"));
            }
        });

        qrImage = findViewById(R.id.qr_image);
        activity = this;

        findViewById(R.id.generate_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValue = editText.getText().toString().trim();
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    qrgEncoder.setColorBlack(Color.BLACK);
                    qrgEncoder.setColorWhite(Color.WHITE);
                    try {
                        bitmap = qrgEncoder.getBitmap();
                        readXML(inputValue);
                        qrImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    editText.setError(getResources().getString(R.string.value_required));
                }
            }
        });

        findViewById(R.id.save_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        boolean save = new QRGSaver().save(savePath, editText.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                        String result = save ? "Image Saved" : "Image Not Saved";
                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                        editText.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

    }

    public void readXML(String inputValue){
        HashMap<String, String> values = new HashMap<String, String>();
//        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><kyc>123</kyc><address>test</address><resiFI>asds</resiFI></user>";
        Document xml = convertStringToDocument(inputValue);
        Node user = xml.getFirstChild();
        uid = ((Element) user).getAttribute("uid");
        name = ((Element) user).getAttribute("name");
        gender = ((Element) user).getAttribute("gender");
        birth = ((Element) user).getAttribute("yob");

        /*NodeList childs = user.getChildNodes();
        Node child;
        for (int i = 0; i < childs.getLength(); i++) {
            child = childs.item(i);

            uid = (String) child.getUserData("uid");
            values.put(child.getNodeName(), child.getTextContent());
        }*/

        insertRecord();
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(
                    xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertRecord(){

        if (userDatabase.userDao().isFavorite(uid)){
            Toast.makeText(this, "already exist", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "does not exist", Toast.LENGTH_SHORT).show();
            User user = new User();
            user.setUid(uid);
            user.setBirth(birth);
            user.setImage(bitmap);
            user.setGender(gender);
            user.setName(name);
            userDatabase.userDao().addData(user);
            Toast.makeText(this, "added sucessfully", Toast.LENGTH_LONG).show();
        }


    }

}