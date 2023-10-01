import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import java.io._
import scala.collection.immutable.ListMap
import scala.io.BufferedSource
import scala.io.Source.fromFile

object lab_01 {
  def main(args: Array[String]): Unit = {
    val source: BufferedSource = fromFile("/home/greg/studying/spark/ml-100k/u.data")
    val lines: Seq[Array[String]] = source.getLines.toList
      .map(string => string.split("\t")) //Seq(Array(196, 242, 3, 881250949), Array(186, 302, 3, 891717742), ...)

    source.close()

    var lines_film: Seq[Array[String]] = Seq()

    lines.foreach(x=> if (x(1) == "22") {lines_film = lines_film :+ x}) //our film id = 22

    val hist_film_unsorted = lines_film.groupBy(x => x(2)).mapValues(_.size)
    val hist_all_unsorted = lines.groupBy(x => x(2)).mapValues(_.size)
    val hist_film = ListMap(hist_film_unsorted.toSeq.sortBy(_._1):_*)
    val hist_all = ListMap(hist_all_unsorted.toSeq.sortBy(_._1):_*)

    val m = Map(
      "hist_film" -> hist_film.values.toList,
      "hist_all" -> hist_all.values.toList
    )

//    Required out format: {"hist_film":[11,22,33,44,55],"hist_all":[134,123,782,356,148]}

    val str_json = compact(render(m))

    val file: File = new File("../lab01.json")
    val writer: BufferedWriter = new BufferedWriter(new FileWriter(file))
    writer.write(str_json)
    writer.close()
  }
}