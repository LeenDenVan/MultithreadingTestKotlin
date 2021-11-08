import javafx.application.Platform
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import okhttp3.OkHttpClient
import java.awt.event.ActionEvent
import java.net.URL
import java.util.*
import javax.xml.crypto.Data

class Controller(okHttpClient: OkHttpClient) : Initializable{
    @FXML
    private lateinit var clear: Button

    @FXML
    private lateinit var download: Button

    @FXML
    private lateinit var prog010: Label

    @FXML
    private lateinit var prog011: Label

    @FXML
    private lateinit var prog012: Label

    @FXML
    private lateinit var prog013: Label

    @FXML
    private lateinit var prog014: Label

    @FXML
    private lateinit var progBar010: ProgressBar

    @FXML
    private lateinit var progBar011: ProgressBar

    @FXML
    private lateinit var progBar012: ProgressBar

    @FXML
    private lateinit var progBar013: ProgressBar

    @FXML
    private lateinit var progBar014: ProgressBar

    @FXML
    private lateinit var textFd: TextField

    @FXML
    private lateinit var title010: Label

    @FXML
    private lateinit var title011: Label

    @FXML
    private lateinit var title012: Label

    @FXML
    private lateinit var title013: Label

    @FXML
    private lateinit var title014: Label

    var okHttpClient = okHttpClient

    @Synchronized
    fun setProgress(cnt:Int, title:String, downloaded:Long, length:Long){
        val down = dataConverter(downloaded)
        val all = dataConverter(length)
        val progs = downloaded.toDouble()/length.toDouble()
        val progSt = String.format("%.2f",progs*100)
        when(cnt){
            0 -> {
                title010.text = title
                prog010.text = "${down}/${all} ${progSt}%"
                progBar010.progress = progs
            }
            1 -> {
                title011.text = title
                prog011.text = "${down}/${all} ${progSt}%"
                progBar011.progress = progs
            }
            2 -> {
                title012.text = title
                prog012.text = "${down}/${all} ${progSt}%"
                progBar012.progress = progs
            }
            3 -> {
                title013.text = title
                prog013.text = "${down}/${all} ${progSt}%"
                progBar013.progress = progs
            }
            4 -> {
                title014.text = title
                prog014.text = "${down}/${all} ${progSt}%"
                progBar014.progress = progs
            }
        }
    }

    private fun dataConverter(data:Long):String {
        if(data<1024*1024){
            return "${String.format("%.2f",data.toDouble()/1024.0)}MB"
        }else{
            return "${String.format("%.2f",data.toDouble()/(1024.0*1024.0))}MB"
        }
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        download.setOnAction {
            println("Start Download")
            println("Start Download")
            downloadByBVID(okHttpClient,textFd.text,1,this)
        }
    }

    /*
    fun setBind(cnt:Int, task:Task<DataMessage>){
        when(cnt){
            0 -> {
                title010.textProperty().bind(task.valueProperty().value.)
                prog010.text = "${down}/${all} ${progSt}%"
                progBar010.progress = progs
            }
            1 -> {
                title011.text = title
                prog011.text = "${down}/${all} ${progSt}%"
                progBar011.progress = progs
            }
            2 -> {
                title012.text = title
                prog012.text = "${down}/${all} ${progSt}%"
                progBar012.progress = progs
            }
            3 -> {
                title013.text = title
                prog013.text = "${down}/${all} ${progSt}%"
                progBar013.progress = progs
            }
            4 -> {
                title014.text = title
                prog014.text = "${down}/${all} ${progSt}%"
                progBar014.progress = progs
            }
        }
    }
    */

}