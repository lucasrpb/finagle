package finagle

import java.net.{InetAddress, InetSocketAddress}
import java.nio.charset.StandardCharsets

import com.twitter.finagle.{Service, ServiceFactory, Stack}
import com.twitter.finagle.netty4.Netty4Listener
import com.twitter.finagle.server.{Listener, StackServer, StdStackServer}
import com.twitter.finagle.transport.Transport
import com.twitter.util.registry.{Entry, GlobalRegistry, SimpleRegistry}
import com.twitter.util.{Await, Closable, Duration, Future}
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}

import finagle.grpc._

object Server {

  def main(args: Array[String]): Unit = {

   val service = new Service[User, User] {
      override def apply(request: User): Future[User] = {

        println(s"received ${request}")

        Future.value(User(request.name.toUpperCase(), request.age))
      }
    }

    val server = ProtocolServer.Server().serve(new InetSocketAddress(InetAddress.getLoopbackAddress, 8080)
      , service)

    Await.result(server)
  }

}
