package net.martinprobson.spark

import com.typesafe.config.Config
import org.apache.commons.collections4.MapUtils

import java.util.{Properties, UUID}
import scala.util.Random

object Util {
  val rand = new Random()

  def randomString(min: Int, max: Int): String = {
    rand.alphanumeric.take(50).mkString("")
  }

  def randomUUID(): String = UUID.randomUUID().toString

  /** propsFromConfig - Convert a typesafe config object to a Java properties object.
    */
  def propsFromConfig(config: Config): Properties = {
    import scala.collection.JavaConverters._

    val map: Map[String, Object] = config
      .entrySet()
      .asScala
      .map({ entry =>
        entry.getKey -> entry.getValue.unwrapped()
      })(collection.breakOut)

    MapUtils.toProperties(map.asJava)
  }
}
