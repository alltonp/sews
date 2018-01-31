package im.mange.sews.db.innards

import java.nio.charset.StandardCharsets._
import java.nio.file.Files._
import java.nio.file.StandardCopyOption._
import java.nio.file.StandardOpenOption._
import java.nio.file.{DirectoryStream, Files, Path, Paths}

import scala.reflect.io.File
//import java.nio.file.Path

object Filepath {
  def save(content: String, path: Path)   = write(path, content.getBytes(UTF_8), CREATE, WRITE, TRUNCATE_EXISTING)
  def append(content: String, path: Path) = write(path, content.getBytes(UTF_8), CREATE, WRITE, APPEND)

  def move(source: Path, target: Path): Path = Files.move(source, target, ATOMIC_MOVE)
  def move(source: File, target: File): Path = move(Paths.get(source.path), Paths.get(target.path))

  def create(path: Path) = createFile(path)
  def load(path: Path)   = File(path.toFile).slurp()

  def list(dir: Path, glob: String): List[String] = {
    val stream: DirectoryStream[Path] = Files.newDirectoryStream(dir, glob)
    var result: List[String] = Nil
    stream.forEach(f => result = f.getFileName.toFile.getName :: result)
    result
    //    java.nio.file.Files.walk(dir).iterator().asScala.filter(Files.isRegularFile(_)).foreach(println)
  }
}