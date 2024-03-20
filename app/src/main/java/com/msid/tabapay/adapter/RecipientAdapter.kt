package com.msid.tabapay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.msid.tabapay.model.Recipient
import com.msid.tabapay.R

class RecipientAdapter(
    private val recipients: List<Recipient>,
    private val listener: RecipientAdapterListener // Listener interface
) : RecyclerView.Adapter<RecipientAdapter.RecipientViewHolder>() {

    inner class RecipientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val recipientNameTextView: TextView = itemView.findViewById(R.id.recipientNameTextView)
        private val recipientEmailTextView: TextView = itemView.findViewById(R.id.recipientEmailTextView)
        private val recipientInitialsTextView: TextView = itemView.findViewById(R.id.recipientInitialsTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val recipient = recipients[position]
                listener.onRecipientClicked(recipient)
            }
        }

        fun bind(recipient: Recipient) {
            with(itemView) {
                recipientNameTextView.text = recipient.name
                recipientEmailTextView.text = recipient.email
                val names = recipient.name.split(" ")
                val initials = names.mapNotNull { it.firstOrNull()?.toUpperCase() }.take(2).joinToString("")
                recipientInitialsTextView.text = initials


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipient, parent, false)
        return RecipientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipientViewHolder, position: Int) {
        holder.bind(recipients[position])
    }

    override fun getItemCount(): Int {
        return recipients.size
    }

    interface RecipientAdapterListener {
        fun onRecipientClicked(recipient: Recipient)
    }
}
