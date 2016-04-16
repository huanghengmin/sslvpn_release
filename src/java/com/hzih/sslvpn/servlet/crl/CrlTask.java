package com.hzih.sslvpn.servlet.crl;


public class CrlTask extends Thread {

    private boolean isRun = false;

    public boolean isRun() {
        return isRun;
    }

    public void exit(){
        this.isRun = false;
    }

    private long interval = 0;
    private String url = null;

    public CrlTask(long interval,String url) {
        this.interval = interval;
        this.url = url;
    }

    private CrlTimingUpdate crlTimingUpdate = new CrlTimingUpdate();

    @Override
    public void run() {
        this.isRun = true;
        while (isRun&&interval>0) {
            if(url!=null&&url.length()>0) {
                crlTimingUpdate.down_crl(url);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    //okay
                }
            }
        }
    }
}