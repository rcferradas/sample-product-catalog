package sample.product.product_catalog.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import sample.product.product_catalog.domain.Product
import sample.product.product_catalog.domain.ProductCategory
import sample.product.product_catalog.infrastructure.database.ProductRepository

@ExtendWith(MockitoExtension::class)
class ProductServiceImplTest {
 private val repository: ProductRepository = mock()
 private val service: ProductServiceImpl = ProductServiceImpl(repository)

 @Test
 fun `should return all products when no category is specified`() {
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ProductCategory.ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ProductCategory.ELECTRONICS)
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts()

  assertEquals(2, result.size)
 }

 @Test
 fun `should return only products of category specified`() {
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ProductCategory.ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ProductCategory.ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN)
  )
  whenever(repository.findByCategory(ProductCategory.ELECTRONICS.categoryName)).thenReturn(products.filter { it.category == ProductCategory.ELECTRONICS})

  val result = service.getProducts(ProductCategory.ELECTRONICS)

  verify(repository).findByCategory(ProductCategory.ELECTRONICS.categoryName)
  assertEquals(2, result.size)
  assertTrue(result.all { it.category == ProductCategory.ELECTRONICS.categoryName })
 }

}