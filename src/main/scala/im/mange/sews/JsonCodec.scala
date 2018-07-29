package im.mange.sews

import argonaut.{DecodeJson, EncodeJson}

//TODO: consider splitting these so we can mixin just what we need ..
case class JsonCodec[IN, OUT](decoder: DecodeJson[IN], encoder: EncodeJson[OUT]) {
  import argonaut.{Parse, _}

  def decode(value: String): IN =
    Parse.parse(value) match {
      case Left(e) => throw new RuntimeException(s"error $e parsing: $value")
      case Right(json) =>
        val result: DecodeResult[IN] = decoder.decodeJson(json)
        result.getOr(throw new RuntimeException(s"error $result decoding: $json"))
    }

  def encode(out: OUT, pretty: Boolean = false): String = {
    val json = encoder.encode(out)
    if (pretty) json.spaces2 else json.nospaces
  }
}
