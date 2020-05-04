package it.agilelab.bigdata.wasp.master.web.controllers

import java.time.Instant
import java.util.Date

import it.agilelab.bigdata.wasp.core.eventengine.eventproducers.SolrEventIndex
import it.agilelab.bigdata.wasp.core.models.{Events, Logs}
import it.agilelab.bigdata.wasp.core.{SolrLoggerIndex, models}
import org.apache.solr.client.solrj.util.ClientUtils

import scala.concurrent.{ExecutionContext, Future}

trait EventsService {
  def events(search: String,
             startTimestamp: Instant,
             endTimestamp: Instant,
             page: Int,
             size: Int): Future[Events]

}

class DefaultSolrEventsService(client: SolrClient)(
  implicit ec: ExecutionContext
) extends EventsService {

  override def events(search: String,
                      startTimestamp: Instant,
                      endTimestamp: Instant,
                      page: Int,
                      size: Int): Future[Events] = {

    val query =
      s"timestamp:[${startTimestamp.toString} TO ${endTimestamp.toString}]" +
        s" AND all:*${ClientUtils.escapeQueryChars(search)}*"

    client.runPredicate(SolrEventIndex().name, query, size, page).map {
      response =>
        val found = response.getResults.getNumFound
        import scala.collection.JavaConverters._
        Events(
          found,
          entries = response.getResults.asScala.toList.map { document =>
            models.EventEntry(
              document.getFieldValue("eventType").asInstanceOf[String],
              document.getFieldValue("eventId").asInstanceOf[String],
              document.getFieldValue("severity").asInstanceOf[String],
              document.getFieldValue("payload").asInstanceOf[String],
              document.getFieldValue("timestamp").asInstanceOf[Date].toInstant,
              document.getFieldValue("source").asInstanceOf[String],
              document.getFieldValue("sourceId").asInstanceOf[String],
              document.getFieldValue("eventRuleName").asInstanceOf[String]
            )
          }
        )

    }

  }

}
