package book.persistence

import com.google.inject.{Inject, Provider}
import comment.persistence.CommentPersistenceTrait

class BookPersistence @Inject() (val commentPersistence: CommentPersistenceTrait) extends BookPersistenceTrait