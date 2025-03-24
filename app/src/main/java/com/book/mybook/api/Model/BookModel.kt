data class AuthorModel(
    val id: Int,
    val name: String,
    val url: String?
)

data class PublisherModel(
    val id: Int,
    val name: String
)

data class BookModel(
    val id: Int,
    val isbn: String,
    val title: String,
    val subtitle: String?,
    val numberOfPages: String?,
    val publishDate: String?,
    val description: String?,
    val coverImageUrl: String?,
    val createdAt: String,
    val authors: List<AuthorModel>,
    val publishers: List<PublisherModel>,
    val rate: Double
)
