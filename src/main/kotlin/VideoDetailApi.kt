import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.URL

class VideoDetailApi(bvid:String, client: OkHttpClient) {
    val apiurl:String = "http://api.bilibili.com/x/web-interface/view"
    val bvid:String = bvid
    val client = client
    var pageList:ArrayList<VideoDetailRes> = ArrayList()
    init{
        val jsonObj = getJSONObj()
        if(jsonObj != null){
            val code = jsonObj.getInt("code")
            if(code == 0){
                val data:JSONObject = jsonObj.getJSONObject("data")
                val videos = data.getInt("videos")
                val pages:JSONArray = data.getJSONArray("pages")
                val title = data.getString("title")
                if(videos == 1){
                    val cid = data.getLong("cid").toString()
                    pageList.add(VideoDetailRes(cid,1,title))
                }else{
                    for(page in pages){
                        if(page is JSONObject){
                            val cid = page.getLong("cid").toString()
                            val pageI = page.getInt("page")
                            val partN = page.getString("part")
                            pageList.add(VideoDetailRes(cid,pageI,"${title} - ${partN}"))
                        }
                    }
                }
            }else{
                when(code){
                    -400 -> throw Exception("请求错误")
                    -403 -> throw Exception("权限不足")
                    -404 -> throw Exception("视频不存在")
                    620002 -> throw Exception("稿件不存在")
                }
            }
        }
    }

    fun buildURL(): URL {
        return URL("${apiurl}?bvid=${bvid}")
    }

    fun requestBuilder(): Request {
        return Request.Builder()
            .url(buildURL())
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0")
            .addHeader("Origin","https://www.bilibili.com")
            .addHeader("Referer","https://www.bilibili.com")
            .addHeader("Cookie","SESSDATA=b9f514c1%2C1639992518%2Cc18de%2A61")
            .addHeader("Connection","keep-alive")
            .build()
    }

    fun getJSONObj():JSONObject?{
        try{
            val response:Response = client.newCall(requestBuilder()).execute()
            val json:JSONObject = JSONObject(response.body?.string())
            return json
        }catch (e:IOException){e.printStackTrace()}
        return null
    }

    fun getPart(page:Int):VideoDetailRes {
        return pageList[page-1]
    }

    companion object{
        class VideoDetailRes(cid:String, page:Int, title:String){
            val cid = cid
            val page = page
            val title = title
            override fun toString(): String {
                return "P${page}_$title cid:$cid"
            }
        }
    }
}
