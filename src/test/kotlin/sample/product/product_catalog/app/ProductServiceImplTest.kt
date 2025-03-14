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
import kotlin.test.assertContentEquals

private const val SORT_BY_CATEGORY = "category"
private const val SORT_BY_PRICE = "price"
private const val SORT_BY_SKU = "sku"
private const val SORT_BY_DESCRIPTION = "description"

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

 @Test
 fun `should return products sorted by sku`() {
  val expected = listOf(
   ProductDTO("SKU0001","Wireless Mouse",19.99, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0002","4K TV", 499.00, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN.categoryName),
  )
  val products = listOf(
   Product("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN),
   Product("SKU0001", "Wireless Mouse", 19.99, ProductCategory.ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ProductCategory.ELECTRONICS),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_SKU)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return products sorted by price`() {
  val expected = listOf(
   ProductDTO("SKU0001","Wireless Mouse",19.99, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN.categoryName),
   ProductDTO("SKU0002","4K TV", 499.00, ProductCategory.ELECTRONICS.categoryName),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ProductCategory.ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ProductCategory.ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_PRICE)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return products sorted by description`() {
  val expected = listOf(
   ProductDTO("SKU0002","4K TV", 499.00, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN.categoryName),
   ProductDTO("SKU0001","Wireless Mouse",19.99, ProductCategory.ELECTRONICS.categoryName),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ProductCategory.ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ProductCategory.ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_DESCRIPTION)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return products sorted by category`() {
  val expected = listOf(
   ProductDTO("SKU0001","Wireless Mouse",19.99, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0002","4K TV", 499.00, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN.categoryName),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ProductCategory.ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN),
   Product("SKU0002", "4K TV", 499.00, ProductCategory.ELECTRONICS),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_CATEGORY)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return only products of category specified and sorted by price`() {
  val expected = listOf(
   ProductDTO("SKU0002","4K TV", 99.00, ProductCategory.ELECTRONICS.categoryName),
   ProductDTO("SKU0001","Wireless Mouse",499.99, ProductCategory.ELECTRONICS.categoryName),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 499.99, ProductCategory.ELECTRONICS),
   Product("SKU0002", "4K TV", 99.00, ProductCategory.ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, ProductCategory.HOME_KITCHEN)
  )
  whenever(repository.findByCategory(ProductCategory.ELECTRONICS.categoryName)).thenReturn(products.filter { it.category == ProductCategory.ELECTRONICS})

  val result = service.getProducts(ProductCategory.ELECTRONICS, SORT_BY_PRICE)

  verify(repository).findByCategory(ProductCategory.ELECTRONICS.categoryName)
  assertEquals(2, result.size)
  assertTrue(result.all { it.category == ProductCategory.ELECTRONICS.categoryName })
  assertContentEquals(expected,result)
 }

}