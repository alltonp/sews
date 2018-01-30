package im.mange.wase

object Cmd {
  val none: Cmd = () => {}
  def batch(commands: Cmd*): Cmd = () => {commands.foreach(_.run()) }
}

trait Cmd {
  def run() : Unit
}