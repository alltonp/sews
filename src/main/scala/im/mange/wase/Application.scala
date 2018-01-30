package im.mange.wase

import im.mange.wase.innards._
import io.shaka.http.Http.HttpHandler

object Application {
  def apply(port: Int, config: Config): Unit = {
    WebServer(port,
      Handler("/ws/*", JettyWebSocketHandler(config.program)),
      Handler("", NaiveJettyHandler(config.endpoints))
    ).start()
  }
}

case class Config(endpoints: HttpHandler, program: WebSocketProgram)
