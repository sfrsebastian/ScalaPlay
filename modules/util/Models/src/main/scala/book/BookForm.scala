package book.model

/**
  * Created by sfrsebastian on 5/6/17.
  */
case class BookForm(name:String, description:String, ISBN:String, image:String, editorialId:Option[Int])
