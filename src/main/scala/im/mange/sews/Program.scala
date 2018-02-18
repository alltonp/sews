package im.mange.sews

import im.mange.sews.innards.WebSocketProgram

case class Program[IN, MODEL, OUT](private var model: MODEL,
                                   private val update: Update[IN, MODEL, OUT],
                                   private val init: (Subscriber => Option[IN]) = (s: Subscriber) => None,
                                   private val fini: (Subscriber => Option[IN]) = (s: Subscriber) => None,
                                   private val updateDebug: Boolean = false
                                  ) extends WebSocketProgram {

  private [sews] val subscribers = update.subscribers

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
          println("* Error during update: " + e.getMessage ++ "\n" ++ e.getStackTrace.toList.mkString("\n"))
          model = modelBeforeUpdate

          //TODO: should we re-throw these? for closed connections defo not
          //TODO: should we catch and throw errors?
          //TODO: if in dev then we should tell client that an error has occured
      }

      finally {
        if (updateDebug) println(s"- Update: $msg to model now: $model -> was: $modelBeforeUpdate")
      }
    }
  }
}