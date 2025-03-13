package sample.product.product_catalog.app

import org.springframework.stereotype.Service
import sample.product.product_catalog.domain.ProductCategory
import sample.product.product_catalog.infrastructure.database.ProductRepository

interface ProductService {
    fun getProducts(category: ProductCategory?=null):List<ProductDTO>
}

@Service
class ProductServiceImpl(val productRepository: ProductRepository):ProductService{
    override fun getProducts(category: ProductCategory?): List<ProductDTO> {
        val productList = when{
            category != null -> productRepository.findByCategory(category.categoryName)
            else -> productRepository.findAll()
        }
        return productList.map { ProductDTO.fromDomain(it)}
    }

}