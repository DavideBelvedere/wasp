package it.agilelab.bigdata.wasp.core.bl

import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import it.agilelab.bigdata.wasp.core.models.{BatchJobInstanceModel, BatchJobModel, JobStatus}

trait BatchJobInstanceBL {

  def getByName(name: String): Option[BatchJobInstanceModel]

  def insert(instance: BatchJobInstanceModel): BatchJobInstanceModel

  def update(instance: BatchJobInstanceModel): BatchJobInstanceModel

  def all(): Seq[BatchJobInstanceModel]

  def instancesOf(name: String): Seq[BatchJobInstanceModel]
}

trait BatchJobBL {

  def getByName(name: String): Option[BatchJobModel]

  def getAll: Seq[BatchJobModel]

  def update(batchJobModel: BatchJobModel): Unit

  def insert(batchJobModel: BatchJobModel): Unit

  def upsert(batchJobModel: BatchJobModel): Unit

  def deleteByName(name: String): Unit

  def instances(): BatchJobInstanceBL

}





