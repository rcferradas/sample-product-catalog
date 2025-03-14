package sample.product.product_catalog.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import sample.product.product_catalog.domain.Product
import sample.product.product_catalog.domain.ProductCategory.ELECTRONICS
import sample.product.product_catalog.domain.ProductCategory.HOME_KITCHEN
import sample.product.product_catalog.infrastructure.database.ProductRepository
import kotlin.test.assertContentEquals

private const val SORT_BY_CATEGORY = "category"
private const val SORT_BY_PRICE = "price"
private const val SORT_BY_SKU = "sku"
private const val SORT_BY_DESCRIPTION = "description"

class ProductServiceImplTest {
 private val repository: ProductRepository = mock()
 private val service: ProductServiceImpl = ProductServiceImpl(repository)

 @Test
 fun `should return all products when no category is specified`() {
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ELECTRONICS)
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts()

  assertEquals(2, result.size)
 }

 @Test
 fun `should return only products of category specified`() {
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN)
  )
  whenever(repository.findByCategory(ELECTRONICS.categoryName)).thenReturn(products.filter { it.category == ELECTRONICS})

  val result = service.getProducts(ELECTRONICS)

  verify(repository).findByCategory(ELECTRONICS.categoryName)
  assertEquals(2, result.size)
  assertTrue(result.all { it.category == ELECTRONICS.categoryName })
 }

 @Test
 fun `should return products sorted by sku`() {
  val expected = listOf(
   ProductDTO("SKU0001","Wireless Mouse",19.99, ELECTRONICS.categoryName,16.99),
   ProductDTO("SKU0002","4K TV", 499.00, ELECTRONICS.categoryName,424.15),
   ProductDTO("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN.categoryName,22.13),
  )
  val products = listOf(
   Product("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN),
   Product("SKU0001", "Wireless Mouse", 19.99, ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ELECTRONICS),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_SKU)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return products sorted by price`() {
  val expected = listOf(
   ProductDTO("SKU0001","Wireless Mouse",19.99, ELECTRONICS.categoryName,16.99),
   ProductDTO("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN.categoryName,22.13),
   ProductDTO("SKU0002","4K TV", 499.00, ELECTRONICS.categoryName,424.15),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_PRICE)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return products sorted by description`() {
  val expected = listOf(
   ProductDTO("SKU0002","4K TV", 499.00, ELECTRONICS.categoryName,424.15),
   ProductDTO("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN.categoryName,22.13),
   ProductDTO("SKU0001","Wireless Mouse",19.99, ELECTRONICS.categoryName,16.99),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ELECTRONICS),
   Product("SKU0002", "4K TV", 499.00, ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_DESCRIPTION)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return products sorted by category`() {
  val expected = listOf(
   ProductDTO("SKU0001","Wireless Mouse",19.99, ELECTRONICS.categoryName,16.99),
   ProductDTO("SKU0002","4K TV", 499.00, ELECTRONICS.categoryName,424.15),
   ProductDTO("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN.categoryName,22.13),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 19.99, ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN),
   Product("SKU0002", "4K TV", 499.00, ELECTRONICS),
  )
  whenever(repository.findAll()).thenReturn(products)

  val result = service.getProducts(sortBy = SORT_BY_CATEGORY)

  assertEquals(3, result.size)
  assertContentEquals(expected,result)
 }

 @Test
 fun `should return only products of category specified and sorted by price`() {
  val expected = listOf(
   ProductDTO("SKU0002","4K TV", 99.00, ELECTRONICS.categoryName,84.15),
   ProductDTO("SKU0001","Wireless Mouse",499.99, ELECTRONICS.categoryName,424.99),
  )
  val products = listOf(
   Product("SKU0001", "Wireless Mouse", 499.99, ELECTRONICS),
   Product("SKU0002", "4K TV", 99.00, ELECTRONICS),
   Product("SKU0003", "Water Bottle", 29.50, HOME_KITCHEN)
  )
  whenever(repository.findByCategory(ELECTRONICS.categoryName)).thenReturn(products.filter { it.category == ELECTRONICS})

  val result = service.getProducts(ELECTRONICS, SORT_BY_PRICE)

  verify(repository).findByCategory(ELECTRONICS.categoryName)
  assertEquals(2, result.size)
  assertTrue(result.all { it.category == ELECTRONICS.categoryName })
  assertContentEquals(expected,result)
 }

 @Test
 fun `should apply correct discount for Electronics`() {
  val product = Product("SKU0001","Wireless Mouse", 100.0,  ELECTRONICS)
  whenever(repository.findAll()).thenReturn(listOf(product))
  val discounted = service.getProducts(null, null).first()
  assertEquals(85.0, discounted.discountedPrice)
 }

 @Test
 fun `should apply correct discount for Home & Kitchen`() {
  val product = Product("SKU0003", "Water Bottle", 200.0, HOME_KITCHEN)
  whenever(repository.findAll()).thenReturn(listOf(product))
  val discounted = service.getProducts(null, null).first()
  assertEquals(150.0, discounted.discountedPrice)
 }

 @Test
 fun `should apply correct discount for SKU ending in 5`() {
  val product = Product("SKU0005", "Headphones", 300.0, ELECTRONICS)
  whenever(repository.findAll()).thenReturn(listOf(product))
  val discounted = service.getProducts(null, null).first()
  assertEquals(210.0, discounted.discountedPrice)
 }

}