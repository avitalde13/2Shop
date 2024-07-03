import android.os.Bundle
import android.util.Log
import okhttp3.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.R
import com.twoshop.R.*
import okhttp3.*
import java.io.IOException
import org.json.JSONObject

class SinglePostCardFragment : Fragment() {

    private lateinit var post: PostEntity
    private lateinit var image: ImageView
    private lateinit var priceTextView: TextView
    private lateinit var aboutTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var ownerTextView: TextView
    private lateinit var apiTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.single_post_card, container, false)

        // Get the PostEntity object from the arguments
        post = arguments?.getSerializable("post") as PostEntity

        // Find the views in the layout
        image = view.findViewById(R.id.post_image)
        priceTextView = view.findViewById(R.id.post_price)
        aboutTextView = view.findViewById(R.id.post_about)
        locationTextView = view.findViewById(R.id.post_location)
        phoneNumberTextView = view.findViewById(R.id.post_phone)
        ownerTextView = view.findViewById(R.id.post_owner)
        apiTextView = view.findViewById(R.id.Api)

        // Set the values to the views

        Picasso.get().load(post.img).placeholder(R.drawable.place_holder_image)
            .into(image)

        priceTextView.text = post.price
        aboutTextView.text = post.about
        locationTextView.text = post.location
        phoneNumberTextView.text = post.phone
        ownerTextView.text = post.owner

        // Call the API to fetch facts about the specific dog breed
        fetchExchangeRate(post.price.toFloat())

        // Inflate the layout for this fragment
        return view
    }



    private fun fetchExchangeRate(amount : Float) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://currency-conversion-and-exchange-rates.p.rapidapi.com/latest?from=EUR&to=USD")
            .get()
            .addHeader("x-rapidapi-key", "46145be5b6mshbbfeecb4ee46751p126cbejsn9127f9b55d0f")
            .addHeader("x-rapidapi-host", "currency-conversion-and-exchange-rates.p.rapidapi.com")
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()


                // Update UI with the modified response data
                activity?.runOnUiThread {
                    // Format the response and update TextView

                    apiTextView.text = (formatExchangeRate(responseData)*amount).toString()
                    if (apiTextView.text.isEmpty()){
                        apiTextView.text= "Failed to fetch Exchange Rate"
                    }
                }
            }
        })
    }

    private fun formatExchangeRate(responseData: String?): Float {
        if (responseData.isNullOrEmpty()) return 0f

        // Parse the response data
        val jsonObject = JSONObject(responseData)
        val rates = jsonObject.getJSONObject("rates")
        val usdRate = rates.getDouble("USD")

        return usdRate.toFloat()
    }
}
