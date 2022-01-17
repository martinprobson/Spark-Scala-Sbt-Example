package net.martinprobson.spark

object Main extends App with Logging with SparkEnv {

  versionInfo.foreach(v => logger.info(v))
  val url = conf.getString("mysql.url")
  val dbtable = conf.getString("mysql.dbtable")
  val user = conf.getString("mysql.user")
  val password = conf.getString("mysql.password")
  val driver = conf.getString("mysql.driver")

  val dbinfo = s"""
                  |---------------------------------------------------------------------------------
                  | URL : $url
                  | dbtable: $dbtable
                  | user: $user
                  | password: $password
                  | driver: $driver
                  |---------------------------------------------------------------------------------
                  |""".stripMargin

  dbinfo.split("\n").toIndexedSeq.foreach(logger.info(_))
  val platformSegmentMapDF = spark.read
    .format("jdbc")
    .option("url", url)
    .option("dbtable", dbtable)
    .option("user", user)
    .option("password", password)
    .option("driver", driver)
    .load()
  logger.info(s"platform_segment_map count = ${platformSegmentMapDF.count()}")
  logger.info(s"${platformSegmentMapDF.show(false)}")
  logger.info(s"${platformSegmentMapDF.printSchema()}")
  spark.stop()
}
