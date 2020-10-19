package com.example.icarus;

public class FSInfo {
    private String FSInfoSignature;
    private String localSignature;
    private String lastKnownFreeCluster;
    private String nextFreeCluster;
    private String trailingSignature;

    public FSInfo() {
    }

    public void setFSInfoSignature(String FSInfoSignature) {
        this.FSInfoSignature = FSInfoSignature;
    }

    public void setLastKnownFreeCluster(String lastKnownFreeCluster) {
        this.lastKnownFreeCluster = lastKnownFreeCluster;
    }

    public void setLocalSignature(String localSignature) {
        this.localSignature = localSignature;
    }

    public void setNextFreeCluster(String nextFreeCluster) {
        this.nextFreeCluster = nextFreeCluster;
    }

    public void setTrailingSignature(String trailingSignature) {
        this.trailingSignature = trailingSignature;
    }

    public String getFSInfoSignature() {
        return FSInfoSignature;
    }

    public String getLastKnownFreeCluster() {
        return lastKnownFreeCluster;
    }

    public String getLocalSignature() {
        return localSignature;
    }

    public String getNextFreeCluster() {
        return nextFreeCluster;
    }

    public String getTrailingSignature() {
        return trailingSignature;
    }

    public Boolean chkFSINFOValidity(){
        if (this.getFSInfoSignature().equals("41615252") && this.getTrailingSignature().equals("0000AA55")) {
            return true;
        }
        else {
            return false;
        }
    }
}
