package com.example.optimizasyon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMatrisUznlk;
    private Button buttonAdim, buttonHesapla;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private TextView textViewAdim;
    private TableLayout tablo;

    private int elemanGenislik;
    private int elemanUzunluk;
    private static int layerUzunluk;
    private static int layerGenislik;
    private int[][] veriler;
    private int boyut;
    private boolean veriAlindiMi = false;

    private FloydWarshall floydWarshall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bagla();
        floydWarshall = new FloydWarshall(MainActivity.this);
    }

    private void Bagla()
    {
        editTextMatrisUznlk = findViewById(R.id.editTextUzunluk);
        buttonAdim = findViewById(R.id.buttonAdim);
        buttonHesapla = findViewById(R.id.buttonHesapla);
        linearLayout = findViewById(R.id.layoutMatris);
        textViewAdim = findViewById(R.id.textViewAdim);
        buttonAdim.setOnClickListener(adimListener);
        buttonHesapla.setOnClickListener(sonucListener);
        editTextMatrisUznlk.setOnEditorActionListener(editTextTikListener);
    }

    void verileriAl()
    {
        veriler = new int[boyut][boyut];
        EditText temp;
        for(int i=0;i<boyut;i++)
        {
            TableRow row = (TableRow)tablo.getChildAt(i);
            for(int j=0;j<boyut;j++)
            {
                temp = ((EditText)row.getChildAt(j));
                if(temp.getText().toString().isEmpty() || temp.getText().toString().equals("∞"))
                    veriler[i][j] = FloydWarshall.SONSUZ;
                else
                    veriler[i][j] = Integer.parseInt(temp.getText().toString());
            }
        }
    }

    public void editTextTemizle()
    {
        EditText temp;
        for(int i=0;i<boyut;i++)
        {
            TableRow row = (TableRow)tablo.getChildAt(i);
            for(int j=0;j<boyut;j++)
            {
                temp = ((EditText)row.getChildAt(j));
                temp.setText("");
            }
        }
    }

    View.OnClickListener adimListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!veriAlindiMi)
            {
                verileriAl();
                floydWarshall.yeniMatrisOlustur(veriler);
            }
            if(!floydWarshall.SonrakiAdim())
                return;
            veriler = floydWarshall.getMatris();
            editTextTemizle();
            verileriGoster(floydWarshall.getDegisimler());
            if(floydWarshall.sonrakiAdimVarMi())
                textViewAdim.setText(floydWarshall.getAdimSayisi() + ". Adim icin Değerler");
            else
                textViewAdim.setText("Sonuc Hesaplandı");
            textViewAdim.setVisibility(View.VISIBLE);
            veriAlindiMi = true;
        }
    };

    View.OnClickListener sonucListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int temp[][];
            if(veriAlindiMi == true)
            {
                temp = floydWarshall.kopyala(veriler);
                while(floydWarshall.SonrakiAdim());
                floydWarshall.degisimHesapla(temp);
                veriler = floydWarshall.getMatris();
            }
            else
            {
                verileriAl();
                floydWarshall.yeniMatrisOlustur(veriler);
                floydWarshall.TamamaniHesapla();
                veriler = floydWarshall.getMatris();
            }

            editTextTemizle();
            verileriGoster(floydWarshall.getDegisimler());
            textViewAdim.setText("Sonuç Hesaplandı.");
            textViewAdim.setVisibility(View.VISIBLE);
        }
    };

    private void verileriGoster(boolean[][] degisimler)
    {
        EditText temp;
        for(int i=0;i<boyut;i++)
        {
            TableRow row = (TableRow)tablo.getChildAt(i);
            for(int j=0;j<boyut;j++)
            {
                temp = ((EditText)row.getChildAt(j));
                if(degisimler[i][j] == true)
                    temp.setTextColor(Color.RED);
                else
                    temp.setTextColor(Color.WHITE);
                if(veriler[i][j] == FloydWarshall.SONSUZ)
                    temp.setText("∞");
                else
                    temp.setText(String.valueOf(veriler[i][j]));
            }
        }
    }

    private void EkranaSigdir(int matrisUzunluk)
    {
        layerUzunluk = linearLayout.getHeight();
        layerGenislik = linearLayout.getWidth();
        elemanGenislik = layerGenislik/matrisUzunluk;
        elemanUzunluk = layerUzunluk/matrisUzunluk;
    }

    TextView.OnEditorActionListener editTextTikListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(actionId == EditorInfo.IME_ACTION_DONE && !editTextMatrisUznlk.getText().toString().isEmpty())
            {
                if(tablo != null)
                {
                    linearLayout.removeAllViews();
                }
                tablo = new TableLayout(MainActivity.this);
                boyut = Integer.parseInt(editTextMatrisUznlk.getText().toString());
                EkranaSigdir(boyut);
                for(int i = 0; i < boyut; i++)
                {
                    TableRow satir = new TableRow(MainActivity.this);
                    satir.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    for(int j=0;j<boyut;j++)
                    {
                        EditText editText = new EditText(MainActivity.this);

                        editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                        editText.setWidth(elemanGenislik);
                        editText.setHeight(elemanUzunluk);
                        editText.getBackground().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        editText.setTextColor(Color.WHITE);
                        editText.setText("");
                        satir.addView(editText);
                    }
                    tablo.addView(satir);
                }
                linearLayout.addView(tablo);
                textViewAdim.setVisibility(View.INVISIBLE);
                veriAlindiMi = false;
            }
            return false;
        }
    };


}
