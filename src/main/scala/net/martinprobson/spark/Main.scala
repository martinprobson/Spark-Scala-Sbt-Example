package net.martinprobson.spark

object Main extends App with Logging with SparkEnv {

  val titlesDF = spark.read.json(getClass.getResource("/data/titles.json").getFile)
  val employeesDF = spark.read.json(getClass.getResource("/data/employees.json").getFile)
  val empTitlesDF = employeesDF.join(titlesDF, Seq("emp_no"), "inner")
  logger.info(s"empTitlesDF count = ${empTitlesDF.count()}")
  versionInfo.foreach(v => logger.info(v))
  spark.stop()
}
