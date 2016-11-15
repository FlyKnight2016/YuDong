package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.bean.Article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文章阅读界面
 */
public class ArticleReadActivity extends MyBaseActivity {
    private Article article;
    private String fileName;//文件名称
    private String fileDir;//文件路径

    @BindView(R.id.tv_article_articleTitle)
    TextView tvArticleArticleTitle;
    @BindView(R.id.tv_article_articleContent)
    TextView tvArticleArticleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        fileName = bundle.getString("newFileName");
        fileDir = bundle.getString("newFileDir");
        if (fileName.equals("2017招生密码")) {
            tvArticleArticleTitle.setText(fileName);

            tvArticleArticleContent.setText("word文档下载链接："+fileDir +
                    "(可将此链接复制发送给电脑直接下载word文档)");
        }else {
            initData();
        }
    }

    /**
     * 读取txt文档
     * @param filePath
     */
    public String readTxtFile(String filePath){
        try {
            String encoding="GBK";
            StringBuffer txt = new StringBuffer();
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt.append(lineTxt);
                }
                read.close();
                return txt.toString();
            }else{
                showToast("找不到指定的文件");
            }
        } catch (Exception e) {
            showToast("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }

    private void initData() {
        tvArticleArticleTitle.setText("2017招生密码");

        tvArticleArticleContent.setText(readTxtFile(fileDir+"/"+fileName));
    }

    @OnClick(R.id.iv_article_back)
    public void onClick() {
        finish();
    }
}
