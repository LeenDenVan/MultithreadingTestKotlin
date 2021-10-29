import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.OutputStream
import java.net.URL
import java.util.*

fun main(args: Array<String>) {
    //Application.launch(Main::class.java, *args)
    val bvid:String = "BV1XV411U7vW"
    val okHttpClient:OkHttpClient = OkHttpClient()
    val downloadReq:DownloadReq = DownloadReq(226136759,116, bvid,1)
    var reqJson:JSONObject? = downloadReq.sendRequest(okHttpClient)
    var outFile:File = File(bvid+".flv")
    var outputStream:OutputStream = outFile.outputStream()
    if(reqJson != null){
        val urls:Array<URL?>? = downloadReq.videoStreamUrlGetter(reqJson)
        if(urls != null){
            for(url in urls){
                println(url)
                val fd:FileDownloader = FileDownloader()
                if(url is URL){
                    fd.download(url,okHttpClient,outputStream)
                }
            }
        }
    }
}

class Main: Application() {
    override fun start(p0: Stage?) {
        var root:Parent = FXMLLoader.load(javaClass.getResource("sample.fxml"))
        var scene: Scene = Scene(root)
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