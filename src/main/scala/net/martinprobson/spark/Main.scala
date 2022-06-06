package net.martinprobson.spark

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.SaveMode

object Main extends App with Logging with SparkEnv {
  import spark.implicits._

  case class UserEvent(id: String, eventTimeStamp: String, name: String)

  spark.sqlContext
    .range(1, 1001)
    .repartition(100)
    .map(r =>
      UserEvent(
        Util.randomUUID(),
        java.time.Clock.systemUTC().instant().toString,
        Util.randomString(10, 100)
      )
    )
    .map(ue => (ue.id, ue.toString))
    .withColumnRenamed("_1", "key")
    .withColumnRenamed("_2", "value")
    .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    .write
    .format("kafka")
    .option(
      "kafka.bootstrap.servers",
      "kafka1.test.local:9095,kafka2.test.local:9096,kafka3.test.local:9097"
    )
    .option("topic", "Topic100")
    .save()

  logger.info("Done")
  scala.io.StdIn.readLine()
  spark.stop()
}
