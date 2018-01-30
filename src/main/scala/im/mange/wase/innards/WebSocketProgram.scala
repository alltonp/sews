package im.mange.wase.innards

import im.mange.wase.Subscriber

trait WebSocketProgram {
  private [wase] val subscribers: Subscribers

  def onInit(subscriber: Subscriber): Unit
  def onFini(subscriber: Subscriber): Unit
  def onMessage(message: String, from: Subscriber): Unit

  def init(subscriber: Subscriber): Unit = {
    subscribers.subscribe(subscriber)
    onInit(subscriber)
  }

  def fini(subscriber: Subscriber): Unit = {
    subscribers.unsubscribe(subscriber)
    onFini(subscriber)
  }
}