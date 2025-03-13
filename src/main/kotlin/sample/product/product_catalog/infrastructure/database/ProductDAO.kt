package sample.product.product_catalog.infrastructure.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sample.product.product_catalog.domain.Product
import sample.product.product_catalog.domain.ProductCategory

@Repository
interface ProductDAO: JpaRepository<ProductEntity,String> {
    fun findByCategory(category:String):List<ProductEntity>
}

@Repository
class H2ProductRepository(val productDAO: ProductDAO):ProductRepository{
    override fun findAll(): List<Product> =
        productDAO.findAll().map(ProductEntity::toDomain)

    override fun findByCategory(category: String): List<Product> =
        productDAO.findByCategory(category).map(ProductEntity::toDomain)

}

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    val sku: String ="",
    val price: Double=0.0,
    val description: String="",
    val category: String=""
){
    fun toDomain() = Product(
        sku = sku,
        price = price,
        description = description,
        category = ProductCategory.of(category)
    )
}