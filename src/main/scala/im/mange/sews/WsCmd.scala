package im.mange.sews

case class WsCmd[IN, OUT](codec: JsonCodec[IN, OUT], all: Subscribers) {
  def send(msg: OUT, to: Subscriber): Cmd = () => { all.send(jsonise(msg), to) }
  def sendAll(msg: OUT): Cmd = () => { all.sendAll(jsonise(msg)) }

  private def jsonise(msg: OUT) = codec.encode(msg)
}
