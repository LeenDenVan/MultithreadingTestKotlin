import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.net.URL

class DownloadReq(okHttpClient: OkHttpClient,cid: Long, qn: Int, bvid: String, fourk: Byte) : AutoCloseable {

    val cid:Long = cid
    val qn:Int = qn
    val bvid = bvid
    val fourk = fourk
    var urlList:Array<URL?>? = null
    var sizeList:Array<Long?>? = null

    init{
        val jsonObject = sendRequest(okHttpClient)
        urlList = videoStreamUrlGetter(jsonObject!!)
        sizeList = videoStreamSizeGetter(jsonObject!!)
    }

    fun sendRequest(okHttpClient: OkHttpClient):JSONObject?{
        val request:Request = Request.Builder().url(URL(urlBuilder(cid, qn, bvid, fourk))).build()
        try{
            val response:Response = okHttpClient.newCall(request).execute()
            //println(response.body?.string())
            val json = JSONObject(response.body?.string())
            if(json != null)return json
        }catch (e:IOException){
            e.printStackTrace()
        }
        return null
    }

    fun videoStreamUrlGetter(json:JSONObject):Array<URL?>?{
        val data:JSONObject? = json.getJSONObject("data")
        if(data != null){
            val durl:JSONArray? = data.getJSONArray("durl")
            if(durl != null){
                var res: Array<URL?> = arrayOfNulls<URL>(durl.count())
                var cnt:Int = 0
                for(objs in durl){
                    if(objs is JSONObject){
                        val order:Int = objs.getInt("order")
                        val url:String = objs.getString("url")
                        res[order-1] = URL(url)
                    }
                }
                return res
            }
        }
        return null
    }

    fun videoStreamSizeGetter(json:JSONObject):Array<Long?>?{
        val data:JSONObject? = json.getJSONObject("data")
        if(data != null){
            val durl:JSONArray? = data.getJSONArray("durl")
            if(durl != null){
                var res: Array<Long?> = arrayOfNulls<Long>(durl.count())
                var cnt:Int = 0
                for(objs in durl){
                    if(objs is JSONObject){
                        val order:Int = objs.getInt("order")
                        val url:Long = objs.getLong("size")
                        res[order-1] = url
                    }
                }
                return res
            }
        }
        return null
    }

    private fun urlBuilder(cid:Long,qn:Int,bvid:String,fourk:Byte):String {
        return apiurl + "cid="+cid+"&qn="+qn+"&otype=json&fourk="+fourk+"&bvid="+bvid
    }

    companion object{
        val apiurl:String = "http://api.bilibili.com/x/player/playurl?"
    }

    override fun close() {

    }
}