package com.example.icarus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    TextView txtPath; // CAN REMOVE IF NOT NEEDED
    Button startAnalyseButton;
    TextView testingText;
    Intent myFileIntent;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*** FOR HIDING TOP BAR ***/
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        /*** Detect Start Analyse Button ***/
        startAnalyseButton = (Button)findViewById(R.id.startAnalyseButton);
        startAnalyseButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v){
                Intent openFileIntent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
                openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                openFileIntent.setType("*/*");
                startActivityForResult(openFileIntent, READ_REQUEST_CODE);
            }
        });
    }
    /*** Detect File input ***/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data != null){
                Uri uri = data.getData();

                try {
                    /*** Insert all the FAT reading functions here ***/
                    testingText = (TextView)findViewById(R.id.testingText);
                    MBR mbr = getMBR(uri, 0); // Instantiate new MBR object

                    if (mbr.chkMBRValidity()) {
                        
                        /** Not really impt**/
                        mbr.getPartition1().setEndOfPartition();
                        mbr.getPartition2().setEndOfPartition();
                        mbr.getPartition3().setEndOfPartition();
                        mbr.getPartition4().setEndOfPartition();
                        testingText.setText("");
                        testingText.append("MBR detected.");
                        testingText.append("\n");
                        testingText.append("MBR Disk Identifier: " + mbr.getDiskIdentifer() + "\n");
                        if (!mbr.getPartition1().getPartitionType().equals("Empty")) {
                            mbr.getPartition1().toString(testingText);
                        }
                        if (!mbr.getPartition2().getPartitionType().equals("Empty")) {
                            mbr.getPartition2().toString(testingText);
                        }

                        testingText.append("\n\n\n\n\n\n\n\n\n");
                        if (!mbr.getPartition3().getPartitionType().equals("Empty")) {
                            mbr.getPartition3().toString(testingText);
                        }
                        if (!mbr.getPartition4().getPartitionType().equals("Empty")) {
                            mbr.getPartition4().toString(testingText);
                        }
                        testingText.append("MBR Signature Type: " + mbr.getSignatureType());

                    }
                    else {
                        testingText.setText("");
                        testingText.append("Invalid MBR. Cannot detect.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Unable to read file");
                }
            }
        }

    }

    /***** ***** ***** ***** FUNCTIONS TO GRAB HEX ***** ***** ***** *****/
    /***** ***** ***** ***** FUNCTIONS TO GRAB HEX ***** ***** ***** *****/
    /***** ***** ***** ***** FUNCTIONS TO GRAB HEX ***** ***** ***** *****/
    /***** ***** ***** ***** FUNCTIONS TO GRAB HEX ***** ***** ***** *****/

    /*** Get Hex Data String in Big Endian Mode ***/
    public StringBuilder getBEHexData(Uri uri, int startCount, int endCount) throws IOException {
        int decimalValue;
        StringBuilder hexString = new StringBuilder();

        try {
            InputStream file1 = getContentResolver().openInputStream(uri);
            file1.skip(startCount);
            for (int i = startCount; i<= endCount; i++)
            {
                decimalValue = file1.read();
                hexString.append(String.format("%02X", decimalValue));
            }

            // System.out.println("getBEHexData:" + hexString);
            file1.close();
            return hexString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*** Get Hex Data String in Big Endian Mode ***/
    public StringBuilder getLEHexData(Uri uri, int startCount, int endCount) throws IOException {
        int decimalValue;
        StringBuilder hexString = new StringBuilder();

        try {
            InputStream file1 = getContentResolver().openInputStream(uri);
            file1.skip(startCount);
            for (int i = startCount; i<= endCount; i++)
            {
                decimalValue = file1.read();
                hexString.append(String.format("%02X", decimalValue));
            }

            StringBuilder hexLE = new StringBuilder();
            for (int j = hexString.length(); j != 0; j-=2) {
                hexLE.append(hexString.substring(j-2, j));
            }

            // System.out.println("getLEHexData:" + hexLE);
            file1.close();
            return hexLE;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*** Change Hex to Decimal ***/ //Long is used in scenario when number is too huge.
    public Long getHexToDecimal(StringBuilder hexString) {
        Long decValue = Long.parseLong(String.valueOf(hexString),16);
        // System.out.println("Convert Hex: " + hexString + " to Decimal: " + decValue);
        return decValue;
    }

    /*** Convert Hex to ASCII String  ***/
    public String getHexToASCII(StringBuilder hexString) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < hexString.length(); i+=2) {
            String str = hexString.substring(i, i+2);
            temp.append((char)Integer.parseInt(str, 16));
        }
        return temp.toString();
    }

    /*** Concat two Strings of Hex ***/
    public StringBuilder concatHex(StringBuilder firstHex, StringBuilder secondHex) {
        StringBuilder concatHex = new StringBuilder();
        concatHex.append(firstHex).append(secondHex);
        System.out.println("ConcatHex: " + concatHex);
        return concatHex;
    }

    /*** Change Hex to LE to Decimal ***/
    public Long getHexLEDec(Uri uri, int startCount, int endCount) throws IOException {
        return getHexToDecimal(getLEHexData(uri, startCount, endCount));
    }

    /***** ***** ***** ***** START OF MASTER BOOT RECORD ***** ***** ***** *****/
    /***** ***** ***** ***** START OF MASTER BOOT RECORD ***** ***** ***** *****/
    /***** ***** ***** ***** START OF MASTER BOOT RECORD ***** ***** ***** *****/
    /***** ***** ***** ***** START OF MASTER BOOT RECORD ***** ***** ***** *****/

    /*** Get MBR Status Information ***/
    public MBR getMBR(Uri uri, int startCount) throws IOException {
        MBR mbr = new MBR(getLEHexData(uri, startCount + 440, startCount + 444).toString());
        mbr.setSignatureType(getLEHexData(uri, startCount + 510, startCount + 511).toString());

        mbr.setPartition1(getMBR_PartitionInfo(uri, + 446));
        mbr.getPartition1().setVBR(getVBRInfo(mbr.getPartition1(), uri));
        System.out.println("Partition 1 OEM: ");
        System.out.println(mbr.getPartition1().getVBR().getOEM());

        mbr.setPartition2(getMBR_PartitionInfo(uri, + 462));
        mbr.getPartition2().setVBR(getVBRInfo(mbr.getPartition2(), uri));
        System.out.println("Partition 2 OEM: ");
        System.out.println(mbr.getPartition2().getVBR().getOEM());

        mbr.setPartition3(getMBR_PartitionInfo(uri, + 478));
        mbr.getPartition3().setVBR(getVBRInfo(mbr.getPartition3(), uri));
        System.out.println("Partition 3 OEM: ");
        System.out.println(mbr.getPartition3().getVBR().getOEM());

        mbr.setPartition4(getMBR_PartitionInfo(uri, + 494));
        mbr.getPartition4().setVBR(getVBRInfo(mbr.getPartition4(), uri));
        System.out.println("Partition 4 OEM: ");
        System.out.println(mbr.getPartition4().getVBR().getOEM());

        //Andy need to change here to grab VBR to grab
        //mbr.getPartition1().setFSInfo(getFSINFO(uri, (int)(mbr.getPartition1().getStartOfPartition() + 1) * 512));

        return mbr;
    }

    public Partition getMBR_PartitionInfo (Uri uri, int startCount) throws IOException {
        Partition partition = new Partition();
        partition.setBootableStatus(getLEHexData(uri, startCount+0, startCount+0));
        partition.setPartitionType(getLEHexData(uri, startCount+4, startCount+4));
        partition.setStartOfPartition(getHexLEDec(uri, startCount+8, startCount+11));
        partition.setLenOfPartition(getHexLEDec(uri, startCount+12, startCount+15));

        return partition;
    }

    public FSInfo getFSINFO(Uri uri, int startCount) throws IOException {
        FSInfo fsinfo = new FSInfo();
        fsinfo.setFSInfoSignature(getLEHexData(uri, startCount+0, startCount+3).toString());
        fsinfo.setLastKnownFreeCluster(getLEHexData(uri, startCount+484, startCount+487).toString());
        fsinfo.setLocalSignature(getLEHexData(uri, startCount+488, startCount+491).toString());
        fsinfo.setNextFreeCluster(getLEHexData(uri, startCount+492, startCount+495).toString());
        fsinfo.setTrailingSignature(getLEHexData(uri, startCount+508, startCount+511).toString());
        return fsinfo;
    }

    public VBR getVBRInfo(Partition partition, Uri uri) throws IOException {
        VBR vbr = new VBR();
        int startCount = (int)(partition.getStartOfPartition()*512);
        vbr.setOEM(getHexToASCII(getBEHexData(uri, startCount + 3, startCount + 10)));
        vbr.setBytesPerSector(getHexToDecimal(getLEHexData(uri, startCount + 11, startCount + 12)));
        vbr.setSectorsPerCluster(getHexToDecimal(getLEHexData(uri, startCount + 13, startCount + 13)));
        vbr.setReservedAreaSize(getHexToDecimal(getLEHexData(uri, startCount + 14, startCount + 15)));

        return vbr;
    }

    /*** Get VBR Status Information ***/
//    public VBR getVBR(Partition partition, Uri uri, int startCount) throws IOException {
//        VBR vbr = new VBR();
//        vbr.setOEM(getHexToASCII(getBEHexData(uri, (int)( startCount + vbr.getVBRSector()*512 + 3), (int) (startCount + vbr.getVBRSector()*512 + 10))));
//        return vbr;
//    }


    /**********Print hex with ASCII version**********/
    public void printHexEdit(Uri uri) throws IOException {
        int bytesCount = 0;
        int valueCount = 0;
        StringBuilder SBHex = new StringBuilder();
        StringBuilder SBText = new StringBuilder();
        StringBuilder SBResult = new StringBuilder();

        try {
            InputStream file1 = getContentResolver().openInputStream(uri);

            while (((valueCount = file1.read()) != -1)) {
                SBHex.append(String.format("%02X ", valueCount));
                if (!Character.isISOControl(valueCount)) {
                    SBText.append((char) valueCount);
                } else {
                    SBText.append(".");
                }

                if (bytesCount == 15) {
                    SBResult.append(SBHex).append("      ").append(SBText).append("\n");
                    System.out.println(SBResult);
                    SBResult.setLength(0);
                    SBHex.setLength(0);
                    SBText.setLength(0);
                    bytesCount = 0;
                } else {
                    bytesCount++;
                }
            }

            if (bytesCount != 0) {
                for (; bytesCount < 16; bytesCount++) {
                    SBHex.append("   ");
                }
                SBResult.append(SBHex).append("      ").append("\n");
                System.out.println(SBResult);
                SBResult.setLength(0);
            }

            // out.println(SBResult);
            file1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}