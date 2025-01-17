package com.telkom21.anaksehat.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.FragmentHomeBinding


class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var balitaFragment: BalitaListFragment
    private lateinit var badutaFragment: BadutaListFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        balitaFragment = BalitaListFragment()
        badutaFragment = BadutaListFragment()
        switchButton()
        replaceFragment(balitaFragment)
        addChildStart()
        return binding.root
    }


    private fun switchButton() {
        binding.balitaButton.setOnClickListener {
            binding.balitaButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_blue
                )
            )
            binding.balitaButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_white
                )
            )

            binding.badutaButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_grey
                )
            )
            binding.badutaButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.sec_white
                )
            )

            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.list_wrapper, balitaFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.badutaButton.setOnClickListener {
            binding.badutaButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_blue
                )
            )
            binding.badutaButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_white
                )
            )

            binding.balitaButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_grey
                )
            )
            binding.balitaButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.sec_white
                )
            )

            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(
                R.id.list_wrapper,
                badutaFragment
            ) // Replace container with BalitaListFragment
            transaction.addToBackStack(null) // Add transaction to back stack for navigation
            transaction.commit()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.list_wrapper, fragment)
            .commit()
    }

    private fun addChildStart(){
        binding.addButton.setOnClickListener {
            startActivity(Intent(requireContext(), AddChildActivity::class.java))
        }
    }

}