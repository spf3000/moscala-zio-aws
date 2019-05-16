package images
import java.io.InputStream
import scalaz.zio.Task
import scalaz.zio.ZIO

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.AmazonS3Client

trait ImageLoader {
  def imageLoader: ImageLoader.Service[Any]
}

object ImageLoader {
  trait Service[R] {
    def store(i: InputStream, id: String): ZIO[R, Throwable, Unit]
  }

  case class PutS3(s3: AmazonS3, put: PutObjectRequest)

  def s3Store(i: InputStream, id: String): ZIO[PutS3, Throwable, Unit] =
    for {
      PutS3(s3,request) <- ZIO.environment[PutS3]
    } yield s3.putObject(request)

  class LiveS3Loader(p: PutS3) extends ImageLoader {
    def imageLoader = new Service[Any] {
      def store(i: InputStream, id: String): ZIO[Any, Throwable, Unit] =
        s3Store(i, id).provide(p)
    }
  }

  object imageLoader {
    def store(i: InputStream, id: String): ZIO[ImageLoader, Throwable, Unit] =
      ZIO.accessM(_.imageLoader.store(i, id))
  }
}
