package layers.persistence

import slick.dbio.DBIO

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait ManyToManyPersistence[S2, S] {
  def addEntityToSourceAction(author:S2, book:S):DBIO[Option[S]]

  def removeEntityFromSourceAction (author:S2, book:S):DBIO[Option[S]]

  def replaceEntitiesFromSourceAction(author:S2, books:Seq[S]): DBIO[Seq[S]]
}
