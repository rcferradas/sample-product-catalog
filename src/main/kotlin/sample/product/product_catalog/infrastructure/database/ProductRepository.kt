package sample.product.product_catalog.infrastructure.database

import sample.product.product_catalog.domain.Product

interface ProductRepository {
    fun findAll():List<Product>
    fun findByCategory(category:String):List<Product>
}