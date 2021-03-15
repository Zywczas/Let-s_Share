package com.zywczas.letsshare.fragmentgroups.adapters

//
//class GroupsAdapter (
//    private val itemClick: (Group) -> Unit
//) : ListAdapter<Group, GroupsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Group>() {
//
//    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
//        return oldItem.id == newItem.id
//    }
//}) {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val posterImage: ImageView = itemView.findViewById(R.id.posterImageViewListItem)
//        private val title: TextView = itemView.findViewById(R.id.titleTextViewListItem)
//        private val rate: TextView = itemView.findViewById(R.id.rateTextViewListItem)
//
//        fun bindGroup(group: Group) {
//            title.text = group.title
//            rate.text = String.format(Locale.getDefault(), "%.1f", group.voteAverage)
//            val posterPath = "https://image.tmdb.org/t/p/w200" + group.posterPath
//            picasso.load(posterPath)
//                .error(R.drawable.error_image)
//                .into(posterImage)
//            itemView.setOnClickListener { itemClick(group) }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.groups_list_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindGroup(getItem(position))
//    }
//
//}