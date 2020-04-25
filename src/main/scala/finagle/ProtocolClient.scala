package finagle

import com.twitter.finagle.client.{StackClient, StdStackClient, Transporter}
import com.twitter.finagle.dispatch.SerialClientDispatcher
import com.twitter.finagle.netty4.Netty4Transporter
import com.twitter.finagle.param.ProtocolLibrary
import com.twitter.finagle.transport.{Transport, TransportContext}
import com.twitter.finagle.{Service, ServiceFactory, Stack}
import io.netty.channel.{ChannelHandlerContext, ChannelOutboundHandlerAdapter, ChannelPipeline, ChannelPromise}
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}
import java.net.SocketAddress
import java.nio.charset.StandardCharsets.UTF_8

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.{Delimiters, MessageToMessageEncoder}

object ProtocolClient {

  val protocolLibrary = "string"

  object NoDelimStringPipeline extends (ChannelPipeline => Unit) {
    def apply(pipeline: ChannelPipeline): Unit = {

      pipeline.addLast("lineEncoder", new MessageToMessageEncoder[ByteBuf] {
        override def encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: java.util.List[AnyRef]): Unit = {

          msg.retain()
          msg.writeBytes(Delimiters.nulDelimiter()(0))

          out.add(msg)
        }
      } )


      pipeline.addLast("userEncode", new UserEncoder())
      pipeline.addLast("userDecode", new UserDecoder())
    }
  }

}
