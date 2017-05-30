package layers.logic

import crud.models.{Entity, Row}
import layers.persistence.{CrudPersistence, OneToManyPersistence}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by sfrsebastian on 5/29/17.
  */
trait OneToManyLogic[S2<:Row, S<:Row, T<:Row, K <: Entity[T]] {

  val persistence:CrudPersistence[S, T, K] with OneToManyPersistence[S2,S]

  def inverseOneToManyRelationMapper(destination:S):Option[S2]

  //Si se manejan muchos registros, sobreescribir con uso de predicado en getAllAction
  def getResourcesFromSource(source: S2, start:Option[Int], limit:Option[Int]):Future[Seq[S]] = {
    persistence.runAction(persistence.getAllAction(persistence.table))
      .map(s=>s.filter(e=>inverseOneToManyRelationMapper(e).map(_.id).contains(source.id)).drop(start.getOrElse(0)).take(limit.getOrElse(Int.MaxValue)))
  }

  def updateResourceToSourceRelation(source:Option[S2], destination:S):Future[Option[S]] = {
    persistence.runAction(persistence.updateEntitySourceAction(source, destination))
  }
}
