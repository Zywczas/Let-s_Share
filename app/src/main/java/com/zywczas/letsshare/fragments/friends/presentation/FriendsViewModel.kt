package com.zywczas.letsshare.fragments.friends.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.friends.domain.FriendsRepository
import com.zywczas.letsshare.model.Friend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: FriendsRepository
): BaseViewModel(){

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    fun getFriends() {
        viewModelScope.launch(dispatchersIO){
            if (sessionManager.isNetworkAvailable()){
                repository.getFriends()
                    .buffer(Channel.CONFLATED)
                    .catch { postMessage(R.string.cant_get_friends) }
                    .collect {
                        _friends.postValue(it)
                        repository.saveFriendsLocally(it)
                    }
            } else { postMessage(R.string.connection_problem) }
        }
    }

    fun addFriend(email: String){
        viewModelScope.launch(dispatchersIO) {
            showProgressBar(true)
            when {
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                repository.userEmail() == email -> postMessage(R.string.its_your_email)
                friends.value == null -> postMessage(R.string.cant_get_friends)
                isFriendAlreadyAdded(email) -> postMessage(R.string.you_have_this_friend)
                else -> repository.addFriend(email)?.let { error -> postMessage(error) }
            }
            showProgressBar(false)
        }
    }

    private fun isFriendAlreadyAdded(email: String): Boolean = friends.value!!.any { it.email == email }

}