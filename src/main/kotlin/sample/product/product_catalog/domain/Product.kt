package sample.product.product_catalog.domain

data class Product(
    val sku: String,
    val description: String,
    val price: Double,
    val category : ProductCategory,
) {
}

enum class ProductCategory(val category:String){
    ELECTRONICS("Electronics"),
    HOME_KITCHEN("Home & Kitchen"),
    CLOTHING("Clothing"),
    ACCESSORIES("Accessories"),
    SPORTS("Sports"),
    MUSICAL_INSTRUMENTS("Musical Instr."),
    FOOTWEAR("Footwear"),
    HOME_APPLIANCES("Home Appliances"),
    TOYS_GAMES("Toys & Games"),
    STATIONERY("Stationery");

    companion object{
        fun of(category: String):ProductCategory =
            ProductCategory.entries.find { it.category==category }?:throw IllegalArgumentException("Product Category not allowed: $category")
    }
}