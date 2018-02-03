package im.mange.sews

import im.mange.sews.innards._
import io.shaka.http.Http.HttpHandler

object LaunchApplication {
  def apply(port: Int, config: Config): Unit = {
    WebServer(port,
      Handler("/ws/*", JettyWebSocketHandler(config.program)),
      Handler("", NaiveJettyHandler(config.endpoints))
    ).start()
  }
}

case class Config(endpoints: HttpHandler, program: WebSocketProgram)
