package finagle

import com.twitter.finagle.client.{StackClient, StdStackClient, Transporter}
import com.twitter.finagle.dispatch.SerialClientDispatcher
import com.twitter.finagle.netty4.Netty4Transporter
import com.twitter.finagle.param.ProtocolLibrary
import com.twitter.finagle.transport.{Transport, TransportContext}
import com.twitter.finagle.{Service, ServiceFactory, Stack}
import io.netty.channel.{
  ChannelHandlerContext,
  ChannelOutboundHandlerAdapter,
  ChannelPipeline,
  ChannelPromise
}
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}
import java.net.SocketAddress
import java.nio.charset.StandardCharsets.UTF_8

object StringClient {

  val protocolLibrary = "string"

  object NoDelimStringPipeline extends (ChannelPipeline => Unit) {
    def apply(pipeline: ChannelPipeline): Unit = {
      pipeline.addLast("stringEncode", new StringEncoder(UTF_8))
      pipeline.addLast("stringDecode", new StringDecoder(UTF_8))
    }
  }

}
