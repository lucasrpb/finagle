import java.util

import com.google.protobuf.any.Any
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.{MessageToMessageDecoder, MessageToMessageEncoder}
import finagle.grpc._
import io.netty.buffer.{ByteBuf, ByteBufUtil}

package object finagle {

  class UserEncoder extends MessageToMessageEncoder[User] {
    override def encode(ctx: ChannelHandlerContext, msg: User, out: util.List[AnyRef]): Unit = {
      val buf = ctx.alloc().buffer().retain()
      out.add(buf.writeBytes(Any.pack(msg).toByteArray))

      buf.release()
    }
  }

  class UserDecoder extends MessageToMessageDecoder[ByteBuf] {
    override def decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: util.List[AnyRef]): Unit = {
      val bytes = ByteBufUtil.getBytes(msg).array
      val parsed = Any.parseFrom(bytes)

      parsed match {
        case _ if parsed.is(User) => out.add(parsed.unpack(User))
      }

    }
  }

}
