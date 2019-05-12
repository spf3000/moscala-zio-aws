package images
import java.io.InputStream
import scalaz.zio.Task
import scalaz.zio.ZIO
import java.io.OutputStream

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.AmazonServiceException
import java.io.File
import com.amazonaws.regions.Region
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.AmazonS3Client

trait ImageLoader {
  def imageLoader: ImageLoader.Service[Any]
}

object ImageLoader {
  trait Service[R] {
    def store(i: InputStream, id: String, bucketName: String, s3: AmazonS3): ZIO[R, Throwable, Unit]
  }

  trait S3Loader extends ImageLoader {
    def imageLoader: ImageLoader.Service[Any] =
      new Service[Any] {
        def store(
            i: InputStream,
            id: String,
            bucketName: String,
            s3: AmazonS3
        ): ZIO[Any, Throwable, Unit] = {
          val putObjectRequest = new PutObjectRequest(bucketName, id, i, new ObjectMetadata())
          ZIO.effect(s3.putObject(putObjectRequest))
        }
      }
  }
  object S3Loader extends S3Loader
}
