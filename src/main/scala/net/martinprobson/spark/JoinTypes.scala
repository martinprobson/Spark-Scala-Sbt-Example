package net.martinprobson.spark

object JoinTypes extends App with SparkEnv {
  import spark.implicits.*


  val empsDF = spark.read.json(getClass.getResource("/data/emps.json").getFile).cache()
  val deptsDF = spark.read.json(getClass.getResource("/data/depts.json").getFile).cache()

  spark.sparkContext.setJobDescription("createTempView employee")
  empsDF.createTempView("employee")
  spark.sparkContext.setJobDescription("createTempView department")
  deptsDF.createTempView("department")

  spark.sparkContext.setJobDescription("Show empsDF")
  empsDF.show()
  spark.sparkContext.setJobDescription("Show deptsDF")
  deptsDF.show()

  println("Inner Join")
  spark.sparkContext.setJobDescription("Inner Join")
  spark.sql(
    """SELECT id, name, employee.deptno, deptname
      |FROM employee
      |INNER JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"inner").select($"id",$"name",$"deptno",$"deptname").show()

  println("Left Join")
  spark.sparkContext.setJobDescription("Left Join")
  spark.sql(
    """SELECT id, name, employee.deptno, deptname
      |FROM employee
      |LEFT JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"left").select($"id",$"name",$"deptno",$"deptname").show()

  println("Right Join")
  spark.sparkContext.setJobDescription("Right Join")
  spark.sql(
    """SELECT id, name, employee.deptno, deptname
      |FROM employee
      |RIGHT JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"right").select($"id",$"name",$"deptno",$"deptname").show()

  println("Full Join")
  spark.sparkContext.setJobDescription("Full Join")
  spark.sql(
    """SELECT id, name, employee.deptno, deptname
      |FROM employee
      |FULL JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"full").select($"id",$"name",$"deptno",$"deptname").show()

  println("Cross Join")
  spark.sparkContext.setJobDescription("Cross Join")
  spark.sql(
    """SELECT id, name, employee.deptno, deptname
      |FROM employee
      |CROSS JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"cross").select($"id",$"name",$"deptno",$"deptname").show()

  println("Semi Join")
  spark.sparkContext.setJobDescription("Semi Join")
  spark.sql(
    """SELECT *
      |FROM employee
      |SEMI JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"semi").show()

  println("Anti Join")
  spark.sparkContext.setJobDescription("Anti Join")
  spark.sql(
    """SELECT *
      |FROM employee
      |ANTI JOIN department ON employee.deptno = department.deptno;
      |""".stripMargin).show()

  empsDF.join(deptsDF,Seq("deptno"),"anti").show()

  scala.io.StdIn.readLine()
  spark.stop()
}
