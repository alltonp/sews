package im.mange.sews

import im.mange.sews.innards.Subscribers

trait Update[IN, MODEL, OUT] {
  val msgCodec: JsonCodec[IN, OUT]
  val subscribers: Subscribers

  //TODO: ultimately from will need to be an option, because the system will send updates too ... scheduled tasks/reprobate etc
  def update(msg: IN, model: MODEL, from: Subscriber): (MODEL, Cmd)
}
