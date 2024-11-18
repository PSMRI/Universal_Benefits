package digit.application.util;

import digit.application.config.Configuration;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class DocumentAddHttpClient {

    private final OkHttpClient client;

    private final Configuration configuration;

    @Autowired
    public DocumentAddHttpClient(Configuration configuration) {
        this.client = new OkHttpClient.Builder().build();
        this.configuration = configuration;
    }

    public Response uploadFile(MultipartFile multipartFile, String tenantId, String module, String tag) throws IOException {
        MediaType mediaType = MediaType.parse("multipart/mixed");

        StringBuilder url = new StringBuilder(configuration.getFileStoreHost()).append(configuration.getFileStorePath());

        RequestBody fileBody = RequestBody.create(
                MediaType.parse(multipartFile.getContentType()),
                multipartFile.getBytes()
        );

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", multipartFile.getOriginalFilename(), fileBody)
                .addFormDataPart("tenantId", tenantId)
                .addFormDataPart("module", module)
                .addFormDataPart("tag", tag)
                .build();

        Request request = new Request.Builder()
                .url(String.valueOf(url))
                .post(body)
                .addHeader("Content-Type", "multipart/mixed")
                .build();

        return client.newCall(request).execute();
    }
}
