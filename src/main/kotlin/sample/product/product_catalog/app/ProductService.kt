package sample.product.product_catalog.app

import org.springframework.stereotype.Service
import sample.product.product_catalog.domain.Product
import sample.product.product_catalog.domain.ProductCategory
import sample.product.product_catalog.infrastructure.database.ProductRepository

interface ProductService {
    fun getProducts(category: ProductCategory?=null,sortBy:String?=null):List<ProductDTO>
}

@Service
class ProductServiceImpl(val productRepository: ProductRepository):ProductService{
    override fun getProducts(category: ProductCategory?, sortBy: String?): List<ProductDTO> {
        val productList = when{
            category != null -> productRepository.findByCategory(category.categoryName)
            else -> productRepository.findAll()
        }
        return when (sortBy){
            null -> productList.map { ProductDTO.fromDomain(it)}
            else -> productList.productsSortedBy(sortBy).map { ProductDTO.fromDomain(it)}
        }
    }

    private fun List<Product>.productsSortedBy(sortBy: String):List<Product> = when(sortBy){
        "sku"-> this.sortedBy { it.sku }
        "price"-> this.sortedBy { it.price }
        "description" -> this.sortedBy { it.description }
        "category" -> this.sortedBy { it.category.categoryName }
        else -> this
    }

}