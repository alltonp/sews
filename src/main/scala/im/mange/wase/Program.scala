package im.mange.wase

import im.mange.wase.innards.WebSocketProgram

case class Program[IN, MODEL, OUT](private var model: MODEL,
                                   private val update: Update[IN, MODEL, OUT],
                                   private val init: (Subscriber => Option[IN]) = (s: Subscriber) => None,
                                   private val fini: (Subscriber => Option[IN]) = (s: Subscriber) => None,
                                   private val debug: Boolean = false
                                  ) extends WebSocketProgram {

  private [wase] val subscribers = update.subscribers

  override def onInit(subscriber: Subscriber): Unit = init(subscriber).foreach(doUpdate(_, subscriber))
  override def onFini(subscriber: Subscriber): Unit = fini(subscriber).foreach(doUpdate(_, subscriber))
  override def onMessage(message: String, from: Subscriber): Unit = doUpdate(update.msgCodec.decode(message), from)

  private def doUpdate(msg: IN, from: Subscriber): Unit = {
    val modelBeforeUpdate = model

    synchronized {
      try {
        val (updatedModel, cmd: Cmd) = update.update(msg, model, from)
        model = updatedModel
        cmd.run()
      }

      catch {
        case e: Exception =>
          //TIP: when we stop the server we get a lot of: RemoteEndpoint unavailable, current state [CLOSING], expecting [OPEN or CONNECTED]
          //... maybe should protect against this
          print("* Error during update: " + e.getMessage ++ "\n" ++ e.getStackTrace.toList.mkString("\n"))
          model = modelBeforeUpdate
      }

      finally {
        println(s"- Update: $msg to model now: $model -> was: $modelBeforeUpdate")
      }
    }
  }
}