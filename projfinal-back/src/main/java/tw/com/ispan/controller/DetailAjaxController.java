package tw.com.ispan.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/pages/detail")
public class DetailAjaxController {
    @Autowired
    private DetailService detailService;

    @GetMapping(path = "/{photoid}", produces = { MediaType.IMAGE_JPEG_VALUE })
    public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name = "photoid") Integer photoid) {
        DetailBean detail = detailService.findById(photoid);
        byte[] result = this.photo;
        if (detail != null) {
            result = detail.getPhoto();
        }
        return result;
    }

    private byte[] photo = null;

    @PostConstruct
    public void initialize() throws IOException {
        byte[] buffer = new byte[8192];

        ClassLoader classLoader = getClass().getClassLoader();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedInputStream is = new BufferedInputStream(classLoader.getResourceAsStream("static/images/no-image.jpg"));
        int len = is.read(buffer);
        while (len != -1) {
            os.write(buffer, 0, len);
            len = is.read(buffer);
        }
        is.close();
        this.photo = os.toByteArray();
    }
}
