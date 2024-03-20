package com.msid.tabapay.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msid.tabapay.adapter.RecipientAdapter
import com.msid.tabapay.model.Recipient
import androidx.navigation.fragment.findNavController
import com.msid.tabapay.R


class RecipientListFragment : Fragment(), RecipientAdapter.RecipientAdapterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipientAdapter: RecipientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipient_list, container, false)
        setupRecyclerView(rootView)
        setupViewListeners(rootView)
        return rootView
    }

    private fun setupRecyclerView(rootView: View) {
        recyclerView = rootView.findViewById(R.id.recipientRecyclerView)
        recipientAdapter = RecipientAdapter(getSampleRecipients(), this) // Pass 'this' as the listener
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipientAdapter
        }
    }

    private fun setupViewListeners(rootView: View) {
//        val viewTransactionHistoryButton: View = rootView.findViewById(R.id.viewTransactionHistoryButton)
//        viewTransactionHistoryButton.setOnClickListener {
//            findNavController().navigate(R.id.action_recipientListFragment_to_transactionHistoryFragment)
//        }
    }

    private fun getSampleRecipients(): List<Recipient> {
        return listOf(
            Recipient(1, "user_id_1","John Doe", "john@example.com", R.drawable.profile_image),
            Recipient(2, "user_id_2","Alice Smith", "alice@example.com", R.drawable.profile_image),
            Recipient(3, "user_id_3","Bob Johnson", "bob@example.com", R.drawable.profile_image),
            Recipient(4, "user_id_4","Emily Johnson", "emily@example.com", R.drawable.profile_image),
            Recipient(5, "user_id_5","Michael Brown", "michael@example.com", R.drawable.profile_image),
            Recipient(6, "user_id_6","Sarah Wilson", "sarah@example.com", R.drawable.profile_image),
            Recipient(7, "user_id_7","David Taylor", "david@example.com", R.drawable.profile_image),
        )
    }

    // Handle recipient item click event
    override fun onRecipientClicked(recipient: Recipient) {
        // Implement your logic here when a recipient item is clicked

        val userId:String = recipient.userId
        val name:String = recipient.name

        if (userId != null) {
            // Create a bundle and put the amount and description
            val bundle = Bundle().apply {
                putString("name",name)
                putString("userId", userId)
            }

            // Navigate to transactionfragment with arguments


            findNavController().navigate(
                R.id.action_recipientListFragment_to_transactionHistoryFragment,
                bundle
            )

        }
    }
}
