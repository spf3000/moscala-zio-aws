package images

import java.io.InputStream
import scalaz.zio.ZIO
import scalaz.zio.internal.PlatformLive
import com.amazonaws.services.s3.{AmazonS3ClientBuilder, AmazonS3Client}
import com.amazonaws.regions.Region
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.model.{ObjectMetadata, PutObjectRequest}

import scalaz.zio.internal.PlatformLive
import scalaz.zio.Runtime

object LambdaFunc {
  import ImageLoader._

  def run(i: InputStream, id: String, bucketName: String) = {
    val s3Client = AmazonS3ClientBuilder
      .standard()
      .withRegion("eu-west-1")
      .withCredentials(new ProfileCredentialsProvider())
      .build();
    val putRequest = new PutObjectRequest(bucketName, id, i, new ObjectMetadata())

    val liveS3Load = new LiveS3Loader(PutS3(s3Client, putRequest))
    val runTime    = Runtime(liveS3Load, PlatformLive.Default)
    runTime.unsafeRun(
      imageLoader.store(i, id)
    )
  }

}
