import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.concurrent.Task
import lombok.Setter
import lombok.Getter
import okhttp3.*
import okio.IOException
import java.io.*
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.round

class FileDownloader{

    var length:Long = 0

    fun download(url:URL, client: OkHttpClient, outputStream: OutputStream, size:Long){
        println("Video Size = ${dataConverter(size)}")
        val request:Request = downloadRangeRequestBuilder(url,0,size/10)
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
                            var cnnt:Int = 0
                            while(true){
                                readBytes = input.read(dataBuffer)
                                if(readBytes == -1)break
                                totalBytes += readBytes
                                if(cnnt++%100==0)println("${dataConverter(totalBytes)}/${lengthn}")
                                outputStream.write(dataBuffer,0,readBytes)
                            }
                            println("Download Completed")
                        }catch (e:IOException){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        )
    }

    fun downloadInParts(url:URL, controller: Controller ,client: OkHttpClient, vdetail:VideoDetailApi.Companion.VideoDetailRes, size:Long, format:String){
        println("In downloadInParts")
        this.length = size
        var file:File = File("P${vdetail.page}_${vdetail.title}_${vdetail.cid}.${format}")
        println(file.name)
        file.createNewFile()
        var raf:RandomAccessFile = RandomAccessFile(file,"rw")
        raf.setLength(size)
        raf.close()
        var raf2:RandomAccessFile = RandomAccessFile(file,"rw")
        var rChannel:FileChannel = raf2.channel
        if(size>=30*1024*1024){
            for(i in 0..4){
                var th0:Thread =  Thread(FilePartDownloadThread(url, controller,client, rChannel, i, length / 5, 5, length, file.name))
                th0.start()
            }
        }
        while(compln!=5){}
        println("Download Completed")
        raf.close()
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
}

fun downloadRangeRequestBuilder(vurl: URL,begin:Long?, end:Long): Request {
    println("From ${begin!!} to ${end!!}")
    return Request.Builder()
        .url(vurl)
        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")
        .addHeader("Origin","https://www.bilibili.com")
        .addHeader("Referer","https://www.bilibili.com")
        .addHeader("Cookie","SESSDATA=b9f514c1%2C1639992518%2Cc18de%2A61")
        .addHeader("Connection","keep-alive")
        .addHeader("Range","bytes=$begin-$end")
        .build()
}

@Volatile var compln:Int = 0

class FilePartDownloadThread(durl:URL, controller: Controller ,okHttpClient: OkHttpClient, rChannel: FileChannel, cnt:Int, partSize:Long, totalParts:Int, allLength:Long, title:String) : Task<Void>(){
    var okHttpClient:OkHttpClient = okHttpClient
    var rChannel:FileChannel = rChannel
    val cnt:Int = cnt
    val partSize:Long = partSize
    val totalParts:Int = totalParts
    val allLength:Long = allLength
    val durl = durl
    var controller:Controller = controller
    val ititle:String = title

    override fun call():Void?{
        okHttpClient.newCall(
            downloadRangeRequestBuilder(durl,
                if(cnt==0) 0 else (cnt*partSize + 1),
                if(cnt == totalParts - 1) allLength else (cnt+1)*partSize
            )
        ).enqueue(object :Callback{
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexcepted Error Code $response")

                    val inputStream = response.body?.byteStream()
                    if(inputStream != null) {
                        var bufferedInputStream = BufferedInputStream(inputStream)
                        try{
                            var pos = if(cnt==0) 0 else (cnt*partSize + 1)
                            var dataBuffer = ByteArray(4096)
                            var readBytes = 0
                            var totalBytes = 0
                            while(true){
                                readBytes=bufferedInputStream.read(dataBuffer)
                                if(readBytes == -1)break
                                totalBytes+=readBytes
                                synchronized(rChannel){
                                    rChannel.write(ByteBuffer.wrap(dataBuffer),pos)
                                    println("Thread ${cnt}: ${totalBytes}/${partSize} downloaded")
                                    Platform.runLater{
                                        controller.setProgress(cnt,ititle,totalBytes.toLong(),allLength)
                                    }
                                    pos+=readBytes
                                }
                            }
                            bufferedInputStream.close()
                            inputStream.close()
                            compln++
                        }catch (e:IOException){
                            e.printStackTrace()
                        }
                    }
                }
            }

        })
        return null
    }
}

class DataMessage(title:String, prog:String){
    @Setter @Getter private var title:StringProperty = SimpleStringProperty()
    @Setter @Getter private var prog:StringProperty = SimpleStringProperty()
    init{
        this.title.value = title
        this.prog.value = prog
    }
}