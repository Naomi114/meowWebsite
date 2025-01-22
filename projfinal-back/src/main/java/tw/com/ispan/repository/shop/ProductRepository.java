package tw.com.ispan.repository.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.ProjfinalApplication;
import tw.com.ispan.domain.shop.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    boolean existsByProductName(String productName);

    public static void main(String[] args) {
        SpringApplication.run(ProjfinalApplication.class, args);
    }
}
