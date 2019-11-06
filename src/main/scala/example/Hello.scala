package example

import com.twitter.finagle.Service
import com.twitter.finagle.http.Http
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpVersion, HttpResponseStatus, HttpRequest, HttpResponse}
import java.net.{SocketAddress, InetSocketAddress}
import com.twitter.finagle.builder.{Server, ServerBuilder}

// Define our service: OK response for root, 404 for other paths
val rootService = new Service[HttpRequest, HttpResponse] {
  def apply(request: HttpRequest) = {
    val r = request.getUri match {
      case "/" => new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
      case _ => new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND)
    }
    Future.value(r)
  }
}

// Serve our service on a port
val address: SocketAddress = new InetSocketAddress(10000)
val server: Server = ServerBuilder()
  .codec(Http())
  .bindTo(address)
  .name("HttpServer")
  .build(rootService)