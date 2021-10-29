import okhttp3.*
import okio.IOException
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import kotlin.math.round

class FileDownloader{

    var length:Long = 0

    fun download(url:URL, client: OkHttpClient, outputStream: OutputStream){
        val request:Request = downloadRequestBuilder(url)
        client.newCall(request).enqueue(object :Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if(!response.isSuccessful) throw IOException("Unexcepted code $response")

                    for((name,value) in response.headers){
                        println("$name: $value")
                    }
                    length =  response.header("content-length","1")!!.toLong()
                    val inputStream:InputStream? = response.body?.byteStream()
                    if(inputStream != null){
                        var input:BufferedInputStream = BufferedInputStream(inputStream)
                        try{
                            var dataBuffer:ByteArray = ByteArray(512)
                            var readBytes:Int = 0
                            var totalBytes:Long = 0
                            val lengthn:String = dataConverter(length)
                            while(true){
                                readBytes = input.read(dataBuffer)
                                if(readBytes == -1)break
                                totalBytes += readBytes
                                println("${dataConverter(totalBytes)}/${lengthn}")
                                outputStream.write(dataBuffer,0,readBytes)
                            }
                        }catch (e:IOException){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        )
    }

    fun downloadInParts(url:URL, client: OkHttpClient, ){

    }

    fun dataConverter(data:Long):String {
        if(data<1024*1024){
            return "${String.format("%.2f",data.toDouble()/1024.0)}MB"
        }else{
            return "${String.format("%.2f",data.toDouble()/(1024.0*1024.0))}MB"
        }
    }

    private fun downloadRequestBuilder(vurl: URL): Request {
        return Request.Builder()
            .url(vurl)
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")
            .addHeader("Origin","https://www.bilibili.com")
            .addHeader("Referer","https://www.bilibili.com")
            .addHeader("Cookie","SESSDATA=b9f514c1%2C1639992518%2Cc18de%2A61")
            .addHeader("Connection","keep-alive")
            .build()
    }

    private fun downloadRangeRequestBuilder(vurl: URL,begin:Long?, end:Long): Request {
        return Request.Builder()
            .url(vurl)
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")
            .addHeader("Origin","https://www.bilibili.com")
            .addHeader("Referer","https://www.bilibili.com")
            .addHeader("Cookie","SESSDATA=b9f514c1%2C1639992518%2Cc18de%2A61")
            .addHeader("Connection","keep-alive")
            .addHeader("Range","$begin-$end")
            .build()
    }
}