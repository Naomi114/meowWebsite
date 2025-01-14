package tw.com.ispan.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import tw.com.ispan.domain.shop.ProductBean;
import tw.com.ispan.dto.ProductResponse;
import tw.com.ispan.service.shop.ProductService;
import tw.com.ispan.util.DatetimeConverter;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/{productId}/upload-image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Integer productId,
            @RequestParam("image") MultipartFile image) {

        // 驗證圖片是否有效
        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("圖片文件不能為空");
        }

        try {
            // 呼叫 Service 層處理圖片上傳
            productService.uploadProductImage(productId, image);
            return ResponseEntity.ok("圖片上傳成功");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("商品不存在: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("圖片上傳失敗: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("未知錯誤: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ProductResponse create(@RequestBody String json) {
        ProductResponse responseBean = new ProductResponse();

        JSONObject obj = new JSONObject(json);
        Integer id = obj.isNull("id") ? null : obj.getInt("id");

        if (id == null) {
            responseBean.setSuccess(false);
            responseBean.setMessage("id是必要欄位(bean)");
        } else if (productService.exists(id)) {
            responseBean.setSuccess(false);
            responseBean.setMessage("id已存在(bean)");
        } else {
            ProductBean insert = productService.create(json);
            if (insert == null) {
                responseBean.setSuccess(false);
                responseBean.setMessage("新增失敗(bean)");
            } else {
                responseBean.setSuccess(true);
                responseBean.setMessage("新增成功(bean)");
            }
        }
        return responseBean;
    }

    @PutMapping("/{id}")
    public String modify(@PathVariable Integer id, @RequestBody String entity) {
        JSONObject responseJson = new JSONObject();
        if (id == null) {
            responseJson.put("success", false);
            responseJson.put("message", "Id是必要欄位");
        } else if (!productService.exists(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "Id不存在");
        } else {
            ProductBean product = productService.update(entity);
            if (product == null) {
                responseJson.put("success", false);
                responseJson.put("message", "修改失敗");
            } else {
                responseJson.put("success", true);
                responseJson.put("message", "修改成功");
            }
        }
        return responseJson.toString();
    }

    @DeleteMapping("/{pk}")
    public String remove(@PathVariable("pk") Integer id) {
        JSONObject responseJson = new JSONObject();

        if (id == null) {
            responseJson.put("success", false);
            responseJson.put("message", "id是必要欄位");
        } else if (!productService.exists(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "id不存在");
        } else {
            boolean delete = productService.remove(id);
            if (!delete) {
                responseJson.put("success", false);
                responseJson.put("message", "刪除失敗");
            } else {
                responseJson.put("success", true);
                responseJson.put("message", "刪除成功");
            }
        }

        return responseJson.toString();
    }

    @GetMapping("/{id}")
    public String findByPrimaryKey(@PathVariable(name = "id") Integer id) {
        JSONObject responseJson = new JSONObject();
        JSONArray array = new JSONArray();
        if (id != null) {
            ProductBean product = productService.findById(id);
            if (product != null) {
                String make = DatetimeConverter.toString(product.getMake(), "yyyy-MM-dd");
                JSONObject item = new JSONObject()
                        .put("id", product.getId())
                        .put("name", product.getName())
                        .put("price", product.getPrice())
                        .put("make", make)
                        .put("expire", product.getExpire());
                array = array.put(item);
            }
        }
        responseJson = responseJson.put("list", array);
        return responseJson.toString();
    }

    @PostMapping("/find")
    public ProductResponse find(@RequestBody String json) {

        ProductResponse responseBean = new ProductResponse();

        long count = productService.count(json);
        responseBean.setCount(count);

        List<ProductBean> products = productService.find(json);
        if (products != null && !products.isEmpty()) {
            responseBean.setList(products);
        } else {
            responseBean.setList(new ArrayList<>());
        }

        return responseBean;
    }

}
