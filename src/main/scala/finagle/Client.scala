package finagle

import com.twitter.finagle.Service
import com.twitter.finagle.client.StackClient
import com.twitter.finagle.dispatch.SerialClientDispatcher
import com.twitter.finagle.netty4.Netty4Transporter
import com.twitter.util.{Await, Future}

object Client {

  def main(args: Array[String]): Unit = {

    val addr = new java.net.InetSocketAddress("localhost", 8080)
    val transporter = Netty4Transporter.raw[String, String](StringClient.NoDelimStringPipeline, addr, StackClient.defaultParams)

    val bridge: Future[Service[String, String]] =
      transporter() map { transport =>
        new SerialClientDispatcher[String, String](transport)
      }

    val client = new Service[String, String] {
      def apply(req: String) = bridge flatMap { svc =>
        svc(req) ensure svc.close()
      }
    }

    val start = System.currentTimeMillis()
    val r = Await.result(client("hello\n"))
    val elapsed = System.currentTimeMillis() - start

    println(s"result ${r} elapsed ${elapsed}ms")

  }

}
