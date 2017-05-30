package layers.persistence

import slick.dbio.DBIO

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait OneToManyPersistence[S2, S] {
  def updateEntitySourceAction(source: Option[S2], destination:S):DBIO[Option[S]]
}
