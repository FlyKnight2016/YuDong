package net.zgyejy.yudong.modle;

import net.zgyejy.yudong.R;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class Portrait {
    private int iid;
    private int portraitCode;
    private String portraitUri;//头像地址

    public Portrait(int iid) {
        this.iid = iid;
        switch (iid) {
            case 1:
                portraitCode = R.drawable.portrait1;
                break;
            case 2:
                portraitCode = R.drawable.portrait2;
                break;
            case 3:
                portraitCode = R.drawable.portrait3;
                break;
            case 4:
                portraitCode = R.drawable.portrait4;
                break;
            case 5:
                portraitCode = R.drawable.portrait5;
                break;
            case 6:
                portraitCode = R.drawable.portrait6;
                break;
            case 7:
                portraitCode = R.drawable.portrait7;
                break;
            case 8:
                portraitCode = R.drawable.portrait8;
                break;
            case 9:
                portraitCode = R.drawable.portrait9;
                break;
            case 10:
                portraitCode = R.drawable.portrait10;
                break;
            case 11:
                portraitCode = R.drawable.portrait11;
                break;
            case 12:
                portraitCode = R.drawable.portrait12;
                break;
            case 13:
                portraitCode = R.drawable.portrait13;
                break;
            case 14:
                portraitCode = R.drawable.portrait14;
                break;
            case 15:
                portraitCode = R.drawable.portrait15;
                break;
            case 16:
                portraitCode = R.drawable.portrait16;
                break;
            case 17:
                portraitCode = R.drawable.portrait17;
                break;
            case 18:
                portraitCode = R.drawable.portrait18;
                break;
            case 19:
                portraitCode = R.drawable.portrait19;
                break;
            case 20:
                portraitCode = R.drawable.portrait20;
                break;
            case 21:
                portraitCode = R.drawable.portrait21;
                break;
            case 22:
                portraitCode = R.drawable.portrait22;
                break;
            case 23:
                portraitCode = R.drawable.portrait23;
                break;
            case 24:
                portraitCode = R.drawable.portrait24;
                break;
            default:
                portraitCode = R.drawable.portrait0;
                break;
        }
        if (iid>0&&iid<=24) {
            portraitUri = "http://api.zgyejy.net/image/" + iid + ".png";
        }else {
            portraitUri = "http://api.zgyejy.net/image/pic.jpg";
        }
    }

    public int getIid() {
        return iid;
    }

    public int getPortraitCode() {
        return portraitCode;
    }

    public String getPortraitUri() {
        return portraitUri;
    }
}
