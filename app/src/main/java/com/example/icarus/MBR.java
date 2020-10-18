package com.example.icarus;

import android.net.Uri;

import java.io.IOException;

public class MBR {
    private String diskIdentifer;
    private Partition partition1;
    private Partition partition2;
    private Partition partition3;
    private Partition partition4;
    private String signatureType;

    public MBR() {
    }

    public MBR(String diskIdentifer) {
        this.diskIdentifer = diskIdentifer;
    }

    public void setDiskIdentifer(String diskIdentifer){
        this.diskIdentifer = diskIdentifer;
    }

    public void setPartition1(Partition partition1) {
        this.partition1 = partition1;
    }

    public void setPartition2(Partition partition2) {
        this.partition2 = partition2;
    }

    public void setPartition3(Partition partition3) {
        this.partition3 = partition3;
    }

    public void setPartition4(Partition partition4) {
        this.partition4 = partition4;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public String getDiskIdentifer() {
        return diskIdentifer;
    }

    public Partition getPartition1() {
        return partition1;
    }

    public Partition getPartition2() {
        return partition2;
    }

    public Partition getPartition3() {
        return partition3;
    }

    public Partition getPartition4() {
        return partition4;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public Boolean chkMBRValidity() {
        if (this.getSignatureType().equals("AA55")) {
            return true;
        }
        else {
            return false;
        }
    }


}
