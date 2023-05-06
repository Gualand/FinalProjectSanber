package com.latihan.githubuser.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubuser.ItemsItem
import com.latihan.githubuser.databinding.FragmentFollowBinding
import com.latihan.githubuser.utils.ViewModelFactory
import com.latihan.githubuser.data.Result
import com.latihan.githubuser.ui.adapter.UserAdapter


class FollowFragment : Fragment() {
    private val followViewModel by viewModels<FollowViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recvUserFollow.layoutManager = layoutManager

        if (position == 1) {
            followViewModel.getFollowers(username!!).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setUserFollowers(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            followViewModel.getFollowing(username.toString()).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setUserFollowing(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUserFollowers(userFollowers: List<ItemsItem>?) {
        val adapter = UserAdapter(userFollowers!!)
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
            }
        })
        binding.recvUserFollow.adapter = adapter
    }

    private fun setUserFollowing(userFollowing: List<ItemsItem>?) {
        val adapter = UserAdapter(userFollowing!!)
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
            }
        })
        binding.recvUserFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}