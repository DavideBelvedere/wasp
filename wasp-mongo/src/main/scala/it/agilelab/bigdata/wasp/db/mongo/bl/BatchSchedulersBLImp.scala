package it.agilelab.bigdata.wasp.db.mongo.bl

import it.agilelab.bigdata.wasp.core.bl.BatchSchedulersBL
import it.agilelab.bigdata.wasp.core.models.BatchSchedulerModel
import it.agilelab.bigdata.wasp.db.mongo.WaspMongoDB
import org.bson.BsonBoolean


class BatchSchedulersBLImp(waspDB: WaspMongoDB) extends BatchSchedulersBL {
  private def factory(t: BatchSchedulerModel) = new BatchSchedulerModel(t.name, t.cronExpression, t.batchJob,
    t.options, t.isActive)

  def getActiveSchedulers(isActive: Boolean = true) = {
    waspDB.getAllDocumentsByField[BatchSchedulerModel]("isActive", new BsonBoolean(isActive)).map(factory(_))
  }


  override def persist(schedulerModel: BatchSchedulerModel) = {
    waspDB.insert[BatchSchedulerModel](schedulerModel)
  }
}
