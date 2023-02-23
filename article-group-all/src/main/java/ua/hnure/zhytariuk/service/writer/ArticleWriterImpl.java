package ua.hnure.zhytariuk.service.writer;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ArticleWriterImpl implements ArticleWriter {
    @Override
    public void createNewFolder(String path, String name) {
        new File(path + "/" + name).mkdir();
    }
}
