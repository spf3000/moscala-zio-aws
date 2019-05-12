package images
import java.io.InputStream
import scalaz.zio.ZIO
import scalaz.zio.internal.PlatformLive
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.regions.Region
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client

import scalaz.zio.internal.PlatformLive
import scalaz.zio.Runtime
import scalaz.zio.DefaultRuntime

object LambdaFunc {
  import ImageLoader._

  def run(i: InputStream, id: String, bucketName: String) = {
    val s3Client = AmazonS3ClientBuilder.standard()
              .withRegion("eu-west-1")
              .withCredentials(new ProfileCredentialsProvider())
              .build();

    val runTime = new DefaultRuntime {}
    runTime.unsafeRun(
      S3Loader.imageLoader.store(i,id,bucketName).provide(s3Client)
    )
  }

}
