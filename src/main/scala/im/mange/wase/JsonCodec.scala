package im.mange.wase

import argonaut.{DecodeJson, EncodeJson}

case class JsonCodec[IN, OUT](decoder: DecodeJson[IN], encoder: EncodeJson[OUT]) {
  import argonaut.{Parse, _}

  def decode(value: String): IN =
    Parse.parse(value) match {
      case Left(e) => throw new RuntimeException(s"error $e parsing: $value")
      case Right(json) =>
        val result: DecodeResult[IN] = decoder.decodeJson(json)
        result.getOr(throw new RuntimeException(s"error $result decoding: $json"))
    }

  def encode(out: OUT): String = encoder.encode(out).nospaces
}
