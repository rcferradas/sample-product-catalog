package sample.product.product_catalog.rest

import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sample.product.product_catalog.app.ProductService
import sample.product.product_catalog.domain.ProductCategory

@RestController
@RequestMapping("products")
class ProductController (val productService: ProductService){

    @GetMapping
    fun getProducts(
        @RequestParam("category") category:String?=null,
        @RequestParam("sortBy") sortBy:String?=null,
    ):HttpEntity<*> {
        return HttpEntity(productService.getProducts(category?.let { ProductCategory.of(category)},sortBy))
    }
}