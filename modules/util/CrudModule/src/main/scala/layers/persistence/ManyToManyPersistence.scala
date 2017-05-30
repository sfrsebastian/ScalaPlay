package layers.persistence

import slick.dbio.DBIO

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait ManyToManyPersistence[S2, S] {
  def addEntityToSourceAction(source:S2, destination:S):DBIO[Option[S]]

  def removeEntityFromSourceAction (source:S2, destination:S):DBIO[Option[S]]

  def replaceEntitiesFromSourceAction(source:S2, destination:Seq[S]): DBIO[Seq[S]]
}
