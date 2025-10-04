import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yoonseo.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {

    lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)

        return binding.root
    }


}