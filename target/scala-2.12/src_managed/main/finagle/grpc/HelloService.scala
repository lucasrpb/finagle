
// Generated by Akka gRPC. DO NOT EDIT.
package finagle.grpc

/**
 * #services
 */
trait HelloService {
  
  
  def sayHello(in: finagle.grpc.HelloRequest): scala.concurrent.Future[finagle.grpc.HelloReply]
  
}

object HelloService extends akka.grpc.ServiceDescription {
  val name = "events.HelloService"

  val descriptor: com.google.protobuf.Descriptors.FileDescriptor =
    finagle.grpc.HelloProto.javaDescriptor;

  object Serializers {
    import akka.grpc.scaladsl.ScalapbProtobufSerializer
    
    val HelloRequestSerializer = new ScalapbProtobufSerializer(finagle.grpc.HelloRequest.messageCompanion)
    
    val HelloReplySerializer = new ScalapbProtobufSerializer(finagle.grpc.HelloReply.messageCompanion)
    
  }
}