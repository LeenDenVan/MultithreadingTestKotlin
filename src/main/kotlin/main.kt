import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.RandomAccess

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
    val bvid:String = "BV1XV411U7vW"

    //val downloadReq:DownloadReq = DownloadReq(226136759,116, bvid,1)
    //var reqJson:JSONObject? = downloadReq.sendRequest(okHttpClient)
    //var outFile:File = File(bvid+".flv")
    //var outputStream:OutputStream = outFile.outputStream()
//    val vdi = VideoDetailApi("BV1XV411U7vW",okHttpClient)
//    for(vdr in vdi.pageList){
//        println(vdr)
    //}
    //downloadByBVID(okHttpClient,"BV1XV411U7vW", 1)

    /*
    val downloadReq:DownloadReq = DownloadReq(vdi.pageList[0].cid.toLong(), 116, vdi.bvid, 1)
    var reqJSON:JSONObject? = downloadReq.sendRequest(okHttpClient)
    var outFile:File = File("${vdi.pageList[0].title}.flv")
    var outputStream:OutputStream = outFile.outputStream()
    if(reqJSON!=null){
        downloadI(outputStream,reqJSON,downloadReq,okHttpClient)
    }*/

}

@Volatile var compl:Int = 0

class DownloadThread(inputStream: InputStream, rChannel: FileChannel, cnt:Int, partSize:Long): Thread(){
    private var inputStream:InputStream = inputStream
    private var rChannel = rChannel
    private val cnt:Int = cnt
    private var partSize:Long = partSize
    override fun run(){
            var bb:ByteArray = ByteArray(8192)
            try{
                var readBytes:Int = 0
                var totalBytes:Long = 0
                var pos:Long = 0
                while(true){
                    readBytes = inputStream.read(bb)
                    if(readBytes == -1) break
                    totalBytes += readBytes
                    println("Thread $cnt in ${cnt*partSize + pos}; ${totalBytes}B processed")
                    synchronized(rChannel){
                        rChannel.write(ByteBuffer.wrap(bb),cnt*partSize + pos)
                        pos += readBytes
                    }
                }
                compl++
                //rChannel.close()
            }catch(e:IOException){e.printStackTrace()}
    }
}

fun downloadI(outputStream: OutputStream, requ:JSONObject,downloadReq:DownloadReq,okHttpClient:OkHttpClient){
    if(requ != null){
        val urls:Array<URL?>? = downloadReq.videoStreamUrlGetter(requ)
        val sizes:Array<Long?>? = downloadReq.videoStreamSizeGetter(requ)
        if(urls != null && sizes != null){
            for((index,url) in urls.withIndex()){
                println(url)
                val fd:FileDownloader = FileDownloader()
                val size = sizes[index]
                if(url is URL){
                    println(url)
                    println(size)
                    //fd.download(url,okHttpClient,outputStream,size!!)
                }
            }
        }
    }else{
        throw NullPointerException("Request body can't be null")
    }
}

fun downloadByBVID(okHttpClient: OkHttpClient,bvid:String, part:Int?, controller:Controller){
    val vda:VideoDetailApi = VideoDetailApi(bvid,okHttpClient)
    val pageDet = vda.pageList[part!!-1]
    val downReq = DownloadReq(okHttpClient,pageDet.cid.toLong(), 116, vda.bvid, 1)
    val url = downReq.urlList!![part!!-1]
    val size = downReq.sizeList!![part!!-1]
    val fd = FileDownloader()
    fd.downloadInParts(url!!, controller,okHttpClient,pageDet,size!!,"flv")
}

class Main: Application() {
    override fun start(p0: Stage?) {
        var loader = FXMLLoader(javaClass.getResource("sample.fxml"))
        //val okHttpClient:OkHttpClient = OkHttpClient()
        var controller:Controller = Controller(OkHttpClient())
        loader.setController(controller)
        var pane:Pane = loader.load()
        var scene: Scene = Scene(pane)
        p0?.title="kotlin javafx"
        p0?.scene=scene
        p0?.show()
    }
}

class Counter {
    companion object {
        public var count:Int = 0
        var lock:Object = Object()
    }
}

class AddThread : Thread() {
    override fun run(){
        for(i in 0..10000){
            synchronized(Counter.lock){
                Counter.count++
            }
        }
    }
}

class DecThread : Thread() {
    override fun run(){
        for(i in 0..10000){
            synchronized(Counter.lock){
                Counter.count--
            }
        }
    }
}