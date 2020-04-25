package finagle

import com.twitter.finagle.dispatch.SerialServerDispatcher
import com.twitter.finagle.netty4.Netty4Listener
import com.twitter.finagle.{param, _}
import com.twitter.finagle.server.{StackServer, StdStackServer}
import com.twitter.finagle.transport.{Transport, TransportContext}
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}
import io.netty.handler.codec.{DelimiterBasedFrameDecoder, Delimiters}
import java.nio.charset.StandardCharsets

import finagle.grpc._

object ProtocolServer {
  val protocolLibrary = "user"

  new StringDecoder()

  private object UserServerPipeline extends (ChannelPipeline => Unit) {
    def apply(pipeline: ChannelPipeline): Unit = {
      //pipeline.addLast("line", new DelimiterBasedFrameDecoder(100, Delimiters.lineDelimiter: _*))
      pipeline.addLast("line", new DelimiterBasedFrameDecoder(1 * 1024 * 1024, Delimiters.nulDelimiter(): _*))

      pipeline.addLast("userDecoder", new UserDecoder())
      pipeline.addLast("userEncoder", new UserEncoder())
    }
  }

  case class Server(
                     stack: Stack[ServiceFactory[User, User]] = StackServer.newStack,
                     params: Stack.Params = StackServer.defaultParams + param.ProtocolLibrary(protocolLibrary))
    extends StdStackServer[User, User, Server] {
    protected def copy1(
                         stack: Stack[ServiceFactory[User, User]] = this.stack,
                         params: Stack.Params = this.params
                       ) = copy(stack, params)

    protected type In = User
    protected type Out = User
    protected type Context = TransportContext

    protected def newListener() = Netty4Listener(UserServerPipeline, params)
    protected def newDispatcher(
                                 transport: Transport[In, Out] { type Context <: Server.this.Context },
                                 service: Service[User, User]
                               ) = new SerialServerDispatcher(transport, service)
  }

  def server: Server = Server()
}