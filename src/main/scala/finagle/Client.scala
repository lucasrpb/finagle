package finagle

import com.twitter.finagle.Service
import com.twitter.finagle.client.StackClient
import com.twitter.finagle.dispatch.SerialClientDispatcher
import com.twitter.finagle.netty4.Netty4Transporter
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.util.{Await, Future}

import finagle.grpc._

object Client {

  def main(args: Array[String]): Unit = {

    val addr = new java.net.InetSocketAddress("localhost", 8080)
    val transporter = Netty4Transporter.raw[User, User](ProtocolClient.NoDelimStringPipeline, addr, StackClient.defaultParams)

    val bridge: Future[Service[User, User]] =
      transporter() map { transport =>
        new SerialClientDispatcher[User, User](transport, NullStatsReceiver.get())
      }

    val client = new Service[User, User] {
      def apply(req: User) = bridge flatMap { svc =>
        svc(req) ensure svc.close()
      }
    }

    val start = System.currentTimeMillis()
    val r = Await.result(client(User("Lucas", 31)))
    val elapsed = System.currentTimeMillis() - start

    println(s"result ${r} elapsed ${elapsed}ms")

  }

}
