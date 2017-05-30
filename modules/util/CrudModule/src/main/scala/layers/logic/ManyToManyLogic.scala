package layers.logic

import crud.models.{Entity, Row}
import layers.persistence.{CrudPersistence, ManyToManyPersistence}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by sfrsebastian on 5/29/17.
  */
trait ManyToManyLogic[S2<:Row, S<:Row, T<:Row, K <: Entity[T]] {

  val persistence: CrudPersistence[S, T, K] with ManyToManyPersistence[S2, S]
  def inverseManyToManyRelationMapper(destination:S):Seq[S2]

  def getResourcesFromSource(source: S2):Future[Seq[S]] = {
    persistence.runAction(persistence.getAllAction(persistence.table))
      .map(s=>s.filter(e=>inverseManyToManyRelationMapper(e).map(_.id).contains(source.id)))
  }

  def addResourceToSource(source:S2, destination:S) : Future[Option[S]] = {
    persistence.runAction(persistence.addEntityToSourceAction(source, destination))
  }

  def replaceResourcesFromSource(source:S2, destinations:Seq[S]):Future[Seq[S]] ={
    persistence.runAction(persistence.replaceEntitiesFromSourceAction(source, destinations))
  }

  def removeResourceFromSource(source:S2, destination:S): Future[Option[S]]= {
    persistence.runAction(persistence.removeEntityFromSourceAction(source, destination))
  }
}
