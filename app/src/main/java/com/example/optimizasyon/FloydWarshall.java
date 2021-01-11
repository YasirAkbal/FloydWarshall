package com.example.optimizasyon;

import android.app.Activity;
import android.content.Context;
import android.content.Context;
import android.util.Log;

import java.util.Arrays;


public class FloydWarshall{
    public static final int SONSUZ = 999999;
    private Context context;
    private Activity mainActivity;
    private int[][] matris;
    private int uzunluk;
    private int i;
    private int j;
    private int k;
    private boolean[][] degisimler;

    public FloydWarshall(Context context)
    {
        this.context = context;
        this.mainActivity = ((Activity)context);
    }

    public void yeniMatrisOlustur(int[][] matris)
    {
        i = j = k = 0;
        this.matris = matris; //algoritmanın ilk aşamasındaki atama işlemi burada yapılıyor
        this.uzunluk = matris.length;
        degisimler = new boolean[uzunluk][uzunluk];
    }

    public boolean SonrakiAdim()
    {
        if(!sonrakiAdimVarMi())
            return false;
        for(int i=0;i<uzunluk;i++)
            Arrays.fill(degisimler[i],false);
        for (i = 0; i < uzunluk; i++)
        {
            for (j = 0; j < uzunluk; j++)
            {
                if ((matris[i][k] + matris[k][j] < matris[i][j]) && !((matris[i][j] == SONSUZ) && ((matris[i][k] == SONSUZ && matris[k][j] < 0) || (matris[i][k] < 0 && matris[k][j] == SONSUZ))))
                {
                    matris[i][j] = matris[i][k] + matris[k][j];
                    degisimler[i][j] = true;
                }
            }
        }
        k++;
        return true;
    }

    public int[][] kopyala(int[][] dizi)
    {
        int[][] temp = new int[dizi.length][dizi.length];

        for(int i=0;i<dizi.length;i++)
        {
            for(int j=0;j<dizi.length;j++)
                temp[i][j] = dizi[i][j];
        }
        return temp;
    }

    public void degisimHesapla(int[][] onceki)
    {
        for(int i=0;i<uzunluk;i++)
            Arrays.fill(degisimler[i],false);
        for(int i=0;i<uzunluk;i++)
        {
            for(int j=0;j<uzunluk;j++)
                if(onceki[i][j] != matris[i][j])
                    degisimler[i][j] = true;
        }
    }

    public boolean TamamaniHesapla()
    {
        if(!sonrakiAdimVarMi())
            return false;
        for(int i=0;i<uzunluk;i++)
            Arrays.fill(degisimler[i],false);
        int[][] temp = kopyala(matris);

        for (k = 0; k < uzunluk; k++)
        {
            for (i = 0; i < uzunluk; i++)
            {
                for (j = 0; j < uzunluk; j++)
                {
                    if (matris[i][k] + matris[k][j] < matris[i][j])
                        matris[i][j] = matris[i][k] + matris[k][j];
                }
            }
        }

        for(int i=0;i<uzunluk;i++)
        {
            for(int j=0;j<uzunluk;j++)
                if(temp[i][j] != matris[i][j])
                    degisimler[i][j] = true;
        }
        return true;
    }

    public int[][] getMatris() {
        return matris;
    }

    public int getUzunluk() {
        return uzunluk;
    }

    public int getAdimSayisi()
    {
        return k;
    }

    public boolean sonrakiAdimVarMi()
    {
        return k!=uzunluk;
    }

    public boolean[][] getDegisimler() {
        return degisimler;
    }
}
