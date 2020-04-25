import java.util

import com.google.protobuf.any.Any
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.{MessageToMessageDecoder, MessageToMessageEncoder}
import finagle.grpc._
import io.netty.buffer.ByteBuf

package object finagle {

  class UserEncoder extends MessageToMessageEncoder[User] {
    override def encode(ctx: ChannelHandlerContext, msg: User, out: util.List[AnyRef]): Unit = {
      out.add(ctx.alloc().buffer().writeBytes(Any.pack(msg).toByteArray))
    }
  }

  class UserDecoder extends MessageToMessageDecoder[ByteBuf] {
    override def decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: util.List[AnyRef]): Unit = {
      out.add(Any.parseFrom(msg.array()).unpack(User))
    }
  }

}
