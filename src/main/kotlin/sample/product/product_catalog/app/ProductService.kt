package sample.product.product_catalog.app

import org.springframework.stereotype.Service
import sample.product.product_catalog.domain.Product
import sample.product.product_catalog.domain.ProductCategory
import sample.product.product_catalog.domain.ProductCategory.ELECTRONICS
import sample.product.product_catalog.domain.ProductCategory.HOME_KITCHEN
import sample.product.product_catalog.infrastructure.database.ProductRepository
import java.math.RoundingMode

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
            null -> productList.map { applyDiscountIfApplicable(it)}
            else -> productList.productsSortedBy(sortBy).map { applyDiscountIfApplicable(it)}
        }
    }

    private fun List<Product>.productsSortedBy(sortBy: String):List<Product> = when(sortBy){
        "sku"-> this.sortedBy { it.sku }
        "price"-> this.sortedBy { it.price }
        "description" -> this.sortedBy { it.description }
        "category" -> this.sortedBy { it.category.categoryName }
        else -> this
    }

    private fun applyDiscountIfApplicable(product:Product):ProductDTO {
        val categoryDiscounts = mapOf(
            ELECTRONICS to 0.15,
            HOME_KITCHEN to 0.25
        )
        val categoryDiscount = categoryDiscounts.getOrDefault(product.category,0.0)
        val skuDiscount = if (product.sku.endsWith("5")) 0.30 else 0.0

        val discount = listOf(categoryDiscount, skuDiscount).max()
        return ProductDTO.fromDomain(product,calculateDiscountedPrice(product,discount))
    }

    private fun calculateDiscountedPrice(product: Product, discount: Double)=
        if(discount>0.0)
                (product.price * (1 - discount)).roundTwoDecimals()
        else null

    private fun Double.roundTwoDecimals():Double = this.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()


}